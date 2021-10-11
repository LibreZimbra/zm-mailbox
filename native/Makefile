SRC     = src

BUILD   = build

BUILD_ROOT := $(shell pwd)

ifneq (${JAVA_HOME},)
JAVA_HOME_ = ${JAVA_HOME}
else
JAVA_HOME_ = $(subst /bin/javac,,$(realpath $(shell which javac)))
endif

SHARED := -shared
JAVAINC := -I$(JAVA_HOME_)/include -I$(JAVA_HOME_)/include/linux
SHARED_EXT := so
PUSHED_EXT := so.Linux.i386
CF := -fPIC -g
CC := gcc

all: FORCE
	$(MAKE) $(BUILD)/libzimbra-native.$(SHARED_EXT)

FORCE: ;

$(BUILD)/libzimbra-native.$(SHARED_EXT): $(BUILD)/IO.o $(BUILD)/zjniutil.o
	$(CC) $(CF) $(LIB_OPTS) $(SHARED) -o $@ $^

$(BUILD)/%.o: $(SRC)/native/%.c
	$(CC) $(CF) $(MACDEF) $(JAVAINC) -I$(BUILD) -Wall -Wmissing-prototypes -c -o $@ $<

$(BUILD)/zjniutil.o: $(SRC)/native/zjniutil.c $(SRC)/native/zjniutil.h

$(BUILD)/IO.o: $(SRC)/native/IO.c $(BUILD)/com_zimbra_znative_IO.h $(SRC)/native/zjniutil.h

#
# Hack to copy to destination for use on incremental builds on linux / mac dev environments.
#
#push: all
#	p4 edit ../ZimbraCommon/jars/zimbra-native.jar
#	cp $(BUILD)/zimbra-native.jar ../ZimbraCommon/jars/zimbra-native.jar
#	p4 edit ../ZimbraServer/lib/libzimbra-native.$(PUSHED_EXT)
#	cp $(BUILD)/libzimbra-native.$(SHARED_EXT) ../ZimbraServer/lib/libzimbra-native.$(PUSHED_EXT)

#
# Clean
#
clean:
	$(RM) -r $(BUILD)

.PHONY: all push clean
