//
// Created by david on 17/01/17.
//

#ifndef CLIENT_DATA_H
#define CLIENT_DATA_H
#pragma once
using namespace std;
#include "Packet.h"
#include <string>
#include <vector>
#include <iostream>

using namespace std;

			class DATA : public Packet
			{

			public:
				const int opCode = 3;
				int packetSize = 0;
				int blockNum = 0;
				std::vector<char> data;
				virtual ~DATA();
				DATA(long pSize, int bNum, std::vector<char> data);
				virtual long getPacketSize() override;
				virtual short getBlockNum() override;
				virtual std::vector<char> getData() override;
				virtual string getName() override;
				virtual void printError() override;
				virtual string getErrorMessage() override;
				virtual int getOpCode() override;
			};



#endif //CLIENT_DATA_H
