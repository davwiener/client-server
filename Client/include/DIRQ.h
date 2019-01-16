//
// Created by david on 17/01/17.
//

#ifndef CLIENT_DIRQ_H
#define CLIENT_DIRQ_H
#pragma once

#include "Packet.h"
#include <string>
#include <vector>

			class DIRQ : public Packet
			{

			public:
				const int opCode = 6;

				DIRQ();
				virtual ~DIRQ();
				virtual int getOpCode() override;
				virtual long getPacketSize() override;
				virtual short getBlockNum() override;
				virtual std::vector<char> getData() override;
				virtual string getName() override;
				virtual void printError() override;
				virtual string getErrorMessage() override;
			};

#endif //CLIENT_DIRQ_H
