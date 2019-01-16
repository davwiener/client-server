//
// Created by david on 17/01/17.
//

#ifndef CLIENT_BCAST_H
#define CLIENT_BCAST_H
using namespace std;
#include <string>

class BCAST : public Packet
{

public:
		const int opCode = 9;
		std::string name;
		short add ;
		virtual ~BCAST();
		BCAST(string name, short a);
		virtual int getOpCode() override;
		virtual long getPacketSize() override;
		virtual short getBlockNum() override;
		virtual std::vector<char> getData() override;
		virtual string getName() override;
		virtual void printError() override;
		virtual string getErrorMessage() override;
};


#endif //CLIENT_BCAST_H
