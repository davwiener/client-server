//
// Created by david on 17/01/17.
//

using namespace std;
#include <iostream>
#include <boost/thread.hpp>
#include "../include/Task.h"
#include "../include/serverTask.h"

serverTask::serverTask(ConnectionHandler *connectionHandler): myHandler(connectionHandler),realanswer() ,opcode(0){};

void serverTask::run() {

    while (true) {
        char answer;
        if (!myHandler->getBytes(&answer, 1)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        //answer return only my byte we need to send it every time to encdec and to check if its null or packet.
        realanswer.push_back(answer);
        if (realanswer.size() < 2){
            opcode = 0;
        }
        if (realanswer.size() == 2) {
            char op[2];
            op[0] = realanswer.at(0);
            op[1] = realanswer.at(1);
            opcode = myHandler->bytesToShort(op);

        }
        Packet *pilpilon = myHandler->createPacket(opcode, realanswer);
        if (pilpilon != NULL) {
            myHandler->clientProcess(pilpilon);
            realanswer.clear();
        }
    }
}

serverTask::serverTask(const serverTask &other):
    myHandler(other.myHandler),
    realanswer(other.realanswer)

{}

serverTask::~serverTask() {
    delete myHandler;
}

serverTask &serverTask::operator=(const serverTask &other) {
    if (this!=&other){
        this->myHandler=other.myHandler;
        this->realanswer=other.realanswer;
        this->opcode=this->opcode;
    }
    return *this;
}






