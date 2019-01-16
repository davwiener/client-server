//
// Created by david on 17/01/17.
//

#include "../include/LOGRQ.h"
LOGRQ::LOGRQ(string name):

    name  (name)
{}
int LOGRQ::getOpCode()
{
    return opCode;
}
string LOGRQ::getName()
{
    return name;
}
long LOGRQ::getPacketSize(){
    return -1;
}
vector<char> LOGRQ::getData() {
    std::vector<char>* v=new vector<char> {'2','3'};
    return *v;
}
 void LOGRQ::printError() {
}
 string LOGRQ::getErrorMessage() {
    return "";
}
short LOGRQ::getBlockNum() {
    return -1;
}

LOGRQ::~LOGRQ() {
    delete this;
}