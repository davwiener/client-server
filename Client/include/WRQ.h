//
// Created by david on 17/01/17.
//

#ifndef CLIENT_WRQ_H
#define CLIENT_WRQ_H
#pragma once
using namespace std;
#include "Packet.h"
#include <string>
#include <vector>
#include "Packet.h"
			class WRQ : public Packet
			{

			public:
				const int opCode = 2;
				string name;
				WRQ(string name);
				virtual long getPacketSize() override;
				virtual short getBlockNum() override;
				virtual std::vector<char> getData() override;
				virtual string getName() override;
				virtual void printError() override;
				virtual string getErrorMessage() override;
				virtual int getOpCode() override;
				virtual ~WRQ();
			};

#endif //CLIENT_WRQ_H
