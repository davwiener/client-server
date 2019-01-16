//
// Created by davidor on 1/17/17.
//

using namespace std;

#include <iostream>
#include <boost/thread.hpp>
#include "../include/keyTask.h"

keyTask::keyTask(ConnectionHandler &connectionHandler) : myHandler(connectionHandler) {};

void keyTask::run(){
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        vector <char> temp=myHandler.encodetosend(line);
        //strncpy(temp, line.c_str(), sizeof(temp));
        string pack = line.substr(0,2);
        if (!myHandler.sendBytes(&temp[0],temp.size())) {
            break;
        }
    }
}



