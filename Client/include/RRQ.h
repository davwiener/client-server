//
// Created by david on 17/01/17.
//

#ifndef CLIENT_RRQ_H
#define CLIENT_RRQ_H
#pragma once
using namespace std;
#include "Packet.h"
#include <string>
#include <vector>

			class RRQ : public Packet
			{

			public:
				virtual ~RRQ();
				const int opCode = 2;
				string name;
				RRQ(string name);
				virtual long getPacketSize() override;
				virtual short getBlockNum() override;
				virtual std::vector<char> getData() override;
				virtual string getName() override;
				virtual void printError() override;
				virtual string getErrorMessage() override;
				virtual int getOpCode() override;

			};

#endif //CLIENT_RRQ_H
