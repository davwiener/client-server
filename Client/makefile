# define some Makefile variables for the compiler and compiler flags
# to use Makefile variables later in the Makefile: $()
CC = g++
CFLAGS  = -c -Wall -Weffc++ -std=c++11
LFLAGS  = -L/usr/local/lib
LDFLAGS = -lboost_system -lboost_locale -lboost_thread 

# All Targets
all: TFTPclient 


TFTPclient : bin/Packet.o bin/ConnectionHandler.o bin/ACK.o bin/BCAST.o bin/DATA.o bin/DIRQ.o bin/DISC.o bin/ERROR.o bin/keyTask.o bin/LOGRQ.o bin/RRQ.o bin/serverTask.o bin/Task.o bin/TFTPclient.o bin/WRQ.o 
	@echo 'Building target: TFTPclient '
	@echo 'Invoking: C++ Linker'
	$(CC) -o  bin/TFTPclient bin/*.o $(LFLAGS) $(LDFLAGS)
	@echo 'Finished building target: TFTPclient '
	@echo ' '

# Depends on the source and header files 
# Depends on the source and header files 
bin/TFTPclient.o: src/TFTPclient.cpp src/BCAST.cpp src/DATA.cpp src/DIRQ.cpp src/DISC.cpp src/ERROR.cpp src/keyTask.cpp src/LOGRQ.cpp src/Packet.cpp src/RRQ.cpp
	$(CC) $(CFLAGS) -g -Iinclude -o bin/TFTPclient.o src/TFTPclient.cpp
bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	$(CC) $(CFLAGS) -g -Iinclude -o bin/ConnectionHandler.o src/ConnectionHandler.cpp	
# Depends on the source and header files 
bin/BCAST.o:
	$(CC) $(CFLAGS) -g -Iinclude -o bin/BCAST.o src/BCAST.cpp
bin/DATA.o:
	$(CC) $(CFLAGS) -g -Iinclude -o bin/DATA.o src/DATA.cpp
bin/DIRQ.o:
	$(CC) $(CFLAGS) -g -Iinclude -o bin/DIRQ.o src/DIRQ.cpp
bin/DISC.o:
	$(CC) $(CFLAGS) -g -Iinclude -o bin/DISC.o src/DISC.cpp
bin/ERROR.o:
	$(CC) $(CFLAGS) -g -Iinclude -o bin/ERROR.o src/ERROR.cpp
bin/keyTask.o:
	$(CC) $(CFLAGS) -g -Iinclude -o bin/keyTask.o src/keyTask.cpp
bin/LOGRQ.o:
	$(CC) $(CFLAGS) -g -Iinclude -o bin/LOGRQ.o src/LOGRQ.cpp
bin/RRQ.o:
	$(CC) $(CFLAGS) -g -Iinclude -o bin/RRQ.o src/RRQ.cpp
bin/Packet.o:
	$(CC) $(CFLAGS) -g -Iinclude -o bin/Packet.o src/Packet.cpp
	
	# Depends on the source and header files
bin/serverTask.o: src/serverTask.cpp
	$(CC) $(CFLAGS) -g -Iinclude -o bin/serverTask.o src/serverTask.cpp
	
# Depends on the source and header files 
bin/Task.o: src/Task.cpp
	$(CC) $(CFLAGS) -g -Iinclude -o bin/Task.o src/Task.cpp
	
# Depends on the source and header files 
bin/WRQ.o: src/WRQ.cpp
	$(CC) $(CFLAGS) -g -Iinclude -o bin/WRQ.o src/WRQ.cpp
	

bin/ACK.o: src/ACK.cpp
	$(CC) $(CFLAGS) -g -Iinclude -o bin/ACK.o src/ACK.cpp
	
.PHONY: clean	
	
#Clean the build directory
clean: 
	rm -f bin/*
	
	
	
	
	
	
