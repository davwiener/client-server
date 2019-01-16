//
// Created by snirco on 1/17/17.
//

#ifndef CLIENT_SERVERTASK_H
#define CLIENT_SERVERTASK_H

#include "Task.h"

#include <vector>
#include <string>

class serverTask:public Task {
private:
    ConnectionHandler *myHandler;
    std::vector<char> realanswer ;
    short opcode=0;
public:
    serverTask(ConnectionHandler *connectionHandler);
    void run() ;
    serverTask(const serverTask& other);
    virtual ~serverTask();

    serverTask& operator=(const serverTask &other);
    friend bool operator==(const serverTask &lhs, const serverTask &rhs);

};
#endif //CLIENT_SERVERTASK_H

