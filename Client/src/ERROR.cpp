//
// Created by david on 17/01/17.
//

#include <iostream>
#include "../include/ERROR.h"
ERROR::ERROR(int ErrorCode):ErrorCode (ErrorCode),ErrorMsg()

{

	switch (ErrorCode)
	{
		case 0:
			ErrorMsg = "Not defined, see error message (if any).";
			break;
		case 1:
			ErrorMsg = "File not found RRQ DELRQ of non existing file.";
			break;
		case 2:
			ErrorMsg = "Access violation -File cannot be written.";

			break;
		case 3:
			ErrorMsg = "Disk full or allocation exceeded no room in disc.";

			break;
		case 4:
			ErrorMsg = "Illegal TFTP operation Unknown Opcode.";
			break;
		case 5:
			ErrorMsg = "File already exists File name exists on WRQ.";
			break;
		case 6:
			ErrorMsg = "User not logged in Any opcode received before Login completes.";
			break;
		case 7:
			ErrorMsg = "User already logged in Login username already connected";
			break;
        default:break;
    }
}
int ERROR::getOpCode()
{
	return opCode;
}
void ERROR::printError(){
	std::cout << "Error ";
	std::cout << this->ErrorCode << ": ";
	std::cout << this->ErrorMsg;
	std::cout <<std::endl;
}

short ERROR::getBlockNum() {
	return ErrorCode;
}

string ERROR::getErrorMessage() {
    return this->ErrorMsg;
}

long ERROR::getPacketSize(){
	return -1;
}
vector<char> ERROR::getData() {
	std::vector<char>* v=new vector<char> {'2','3'};
	return *v;
}
 string ERROR::getName() {
	return "";
}

ERROR::~ERROR() {
	delete this;
}