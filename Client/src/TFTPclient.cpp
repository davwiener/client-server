#include <stdlib.h>
#include <iostream>
#include <boost/thread.hpp>
#include "../include/ConnectionHandler.h"
#include "../include/serverTask.h"
#include "../include/keyTask.h"

//
// Created by snirbu on 1/19/17.
//

int main (int argc, char *argv[]) {


    //if (argc < 3) {
    //    std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
    //    return -1;
    //}
    std::string host =argv[1];
    short port =(short)atoi(argv[2]);

    ConnectionHandler *connectionHandler=new ConnectionHandler(host, port);

    if (!connectionHandler->connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    serverTask* task1=new serverTask(connectionHandler);
    keyTask* task2=new keyTask(*connectionHandler);
    boost::thread th1(boost::bind(&serverTask::run, task1));
    boost::thread th2(boost::bind(&keyTask::run, task2));
    th1.join();
    th2.join();
    return 0;
}