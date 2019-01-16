//
// Created by david on 17/01/17.
//

#ifndef CLIENT_LOGRQ_H
#define CLIENT_LOGRQ_H
#pragma once
using namespace std;
#include "Packet.h"
#include <string>
			class LOGRQ : public Packet
			{

			public:
				virtual ~LOGRQ();
				const int opCode = 7;
				string name;
				LOGRQ(string name);
				virtual int getOpCode() override;
				virtual long getPacketSize() override;
				virtual short getBlockNum() override;
				virtual std::vector<char> getData() override;
				virtual string getName() override;
				virtual void printError() override;
				virtual string getErrorMessage() override;
			};

#endif //CLIENT_LOGRQ_H
