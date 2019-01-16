//
// Created by snirco on 1/17/17.
//

#ifndef CLIENT_KEYTASK_H
#define CLIENT_KEYTASK_H
#include "Task.h"
#include "../include/ConnectionHandler.h"
class keyTask :public Task {
private:
    ConnectionHandler &myHandler;
public:
    keyTask(ConnectionHandler &connectionHandler);
    void run();



    bool operator!=(const keyTask &rhs) const;


};
#endif //CLIENT_KEYTASK_H
