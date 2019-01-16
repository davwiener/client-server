cmake_minimum_required(VERSION 3.6)
project(include)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES
        ACK.h
        BCAST.h
        ConnectionHandler.h
        DATA.h
        DIRQ.h
        DISC.h
        ERROR.h
        LOGRQ.h
        Packet.h
        RRQ.h
        Task.h
        WRQ.h)

add_executable(include ${SOURCE_FILES})