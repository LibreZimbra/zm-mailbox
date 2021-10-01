# SPDX-License-Identifier: AGPL-3.0-or-later

ANT_TARGET := all dist

all: build-ant-autover

include build.mk

define install_conf2
mkdir -p $(INSTALL_DIR)/conf && cp $(1) $(INSTALL_DIR)/conf/$(2)
endef

define install_lib
mkdir -p $(INSTALL_DIR)/lib && cp $(1) $(INSTALL_DIR)/lib
endef

install:
	# zimbra-mbox-war
	$(call mk_install_dir, jetty_base/webapps/service)
	$(call mk_install_dir, jetty_base/etc)
	cd $(INSTALL_DIR)/jetty_base/webapps/service && \
		jar -xf $(PWD)/store/build/service.war
	cp store/conf/web.xml.production                      $(INSTALL_DIR)/jetty_base/etc/service.web.xml.in

	# zimbra-common-mbox-conf
	$(call install_conf, milter-conf/conf/milter.log4j.properties)
	$(call install_conf, milter-conf/conf/mta_milter_options.in)
	$(call install_conf, store-conf/conf/datasource.xml)
	$(call install_conf, store-conf/conf/stats.conf.in)
	$(call install_conf, store/conf/unbound.conf.in)
	cp store-conf/conf/localconfig.xml.production         $(INSTALL_DIR)/conf/localconfig.xml
	cp store-conf/conf/log4j.properties.production        $(INSTALL_DIR)/conf/log4j.properties.in

	# zimbra-mbox-conf
	$(call install_conf, store-conf/conf/globs2)
	$(call install_conf, store-conf/conf/magic)
	$(call install_conf, store-conf/conf/magic.zimbra)
	$(call install_conf, store-conf/conf/globs2.zimbra)
	$(call install_conf, store-conf/conf/spnego_java_options.in)
	$(call install_conf, store-conf/conf/contacts/zimbra-contact-fields.xml)
	$(call install_conf, store-conf/conf/common-passwords.txt)

	# zimbra-common-mbox-db
	$(call mk_install_dir, db)
	cp store/build/dist/versions-init.sql                 $(INSTALL_DIR)/db/versions-init.sql

	# zimbra-common-mbox-native-lib
	$(call install_lib, native/build/libzimbra-native.so)

	# zimbra-common-mbox-conf-msgs
	$(call mk_install_dir,conf/msgs)
	cp store-conf/conf/msgs/*.properties                  $(INSTALL_DIR)/conf/msgs

	# zimbra-common-mbox-conf-rights
	$(call mk_install_dir,conf/rights)
	cp store-conf/conf/rights/*.xml                       $(INSTALL_DIR)/conf/rights

	# zimbra-common-mbox-conf-attrs
	$(call mk_install_dir,conf/attrs)
	cp store/conf/attrs/amavisd-new-attrs.xml             $(INSTALL_DIR)/conf/attrs
	cp store/conf/attrs/zimbra-attrs.xml                  $(INSTALL_DIR)/conf/attrs
	cp store/conf/attrs/zimbra-ocs.xml                    $(INSTALL_DIR)/conf/attrs
	$(call install_conf, store/build/dist/conf/attrs/zimbra-attrs-schema)

	# zimbra-common-mbox-docs
	$(call mk_install_dir,docs)
	cp store/docs/* $(INSTALL_DIR)/docs

	# zimbra-common-core-jar
	$(call mk_install_dir,lib/jars)
	cp store/build/dist/zm-store.jar                      $(INSTALL_DIR)/lib/jars/zimbrastore.jar
	cp soap/build/dist/zm-soap.jar                        $(INSTALL_DIR)/lib/jars/zimbrasoap.jar
	cp client/build/dist/zm-client.jar                    $(INSTALL_DIR)/lib/jars/zimbraclient.jar
	cp common/build/dist/zm-common.jar                    $(INSTALL_DIR)/lib/jars/zimbracommon.jar
	cp native/build/dist/zm-native.jar                    $(INSTALL_DIR)/lib/jars/zimbra-native.jar

	# dev symlinks
	ln -s zimbraclient.jar  $(INSTALL_DIR)/lib/jars/zm-client.jar
	ln -s zimbrasoap.jar    $(INSTALL_DIR)/lib/jars/zm-soap.jar
	ln -s zimbrastore.jar   $(INSTALL_DIR)/lib/jars/zm-store.jar
	ln -s zimbracommon.jar  $(INSTALL_DIR)/lib/jars/zm-common.jar
	ln -s zimbra-native.jar $(INSTALL_DIR)/lib/jars/zm-native.jar

clean:
	rm -Rf build client/build common/build native/build soap/build store/build
