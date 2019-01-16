//
// Created by david on 17/01/17.
//

#ifndef CLIENT_TASK_H
#define CLIENT_TASK_H


#include "ConnectionHandler.h"

class Task {
private:
public:
    virtual ~Task();
    virtual void run()= 0;
    Task(){};
};
#endif