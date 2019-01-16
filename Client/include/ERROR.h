//
// Created by david on 17/01/17.
//

#ifndef CLIENT_ERROR_H
#define CLIENT_ERROR_H
#pragma once
using namespace std;
#include "Packet.h"
#include <string>

			class ERROR : public Packet
			{
			public:
				virtual ~ERROR();
				const int opCode = 5;
				short ErrorCode = 0;
				string ErrorMsg;
				ERROR(int ErrorCode);
				virtual int getOpCode() override;
				virtual void printError() override ;
				virtual long getPacketSize() override;
				virtual short getBlockNum() override;
				virtual std::vector<char> getData() override;
				virtual string getName() override;
				virtual string getErrorMessage() override;
			};

#endif //CLIENT_ERROR_H
