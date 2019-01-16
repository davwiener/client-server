//
// Created by david on 17/01/17.
//

#ifndef CLIENT_ACK_H
#define CLIENT_ACK_H

class ACK : public Packet
{


public:;
    const int opCode = 4;
    short blockNum ;
    ACK(short num);
    virtual short getBlockNum();
    virtual int getOpCode() override;
    virtual long getPacketSize() override;
    virtual std::vector<char> getData() override;
    virtual string getName() override;
    virtual void printError() override;
    virtual string getErrorMessage() override;
};

#endif //CLIENT_ACK_H
