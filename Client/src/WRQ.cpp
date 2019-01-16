//
// Created by david on 17/01/17.
//

#include "../include/ERROR.h"
#include "../include/WRQ.h"

WRQ::WRQ(string name):

   name  (name)


{}

string WRQ::getName()
{
    return name;
}

int WRQ::getOpCode()
{
    return opCode;
}
long WRQ::getPacketSize(){
    return -1;
}
vector<char> WRQ::getData() {
    std::vector<char>* v=new vector<char> {'2','3'};
    return *v;
}
 void WRQ::printError() {
}
 string WRQ::getErrorMessage() {
    return "";
}
short WRQ::getBlockNum() {
    return -1;
}

WRQ::~WRQ() {
    delete this;
}