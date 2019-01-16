//
// Created by david on 17/01/17.
//

#ifndef CLIENT_DISC_H
#define CLIENT_DISC_H
#pragma once

#include "Packet.h"

			class DISC : public Packet
			{

			public:
				const int opCode = 10;

				DISC();
				virtual ~DISC();
				virtual int getOpCode() override;
				virtual long getPacketSize() override;
				virtual short getBlockNum() override;
				virtual std::vector<char> getData() override;
				virtual string getName() override;
				virtual void printError() override;
				virtual string getErrorMessage() override;
			};

#endif //CLIENT_DISC_H
