//
// Created by david on 19/01/17.
//

#ifndef CLIENT_CONNECTIONHANDLER_H
#define CLIENT_CONNECTIONHANDLER_H
#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include "../include/WRQ.h"
#include "../include/RRQ.h"
#include "../include/BCAST.h"
#include "../include/ACK.h"
#include "../include/DIRQ.h"
#include "../include/DATA.h"
#include "../include/DISC.h"
#include "../include/ERROR.h"


using boost::asio::ip::tcp;

class ConnectionHandler {

private:
    const std::string host_;
    const short port_;
    boost::asio::io_service io_service_;   // Provides core I/O functionality
    tcp::socket socket_;
    int currdatanum = 1;
    bool names=false;
    long end=0;
    long start=0;
    long datasize=0;
    bool canSend = true;
    bool stopsenddata=false;
    std::string currDataName = "";
    std::vector<char> currentdata;
public:
    bool operator==(const ConnectionHandler &rhs) const;

    bool operator!=(const ConnectionHandler &rhs) const;

private:
    short currOp;

public:
    short bytesToShort(char bytesArr[]);
    short bytes2ShortInTheMiddle(std::vector<char> &byteArr, int startPlace, int endPlace);
    std::vector<char> shortToBytes(short num);
    std::vector <char> encode(Packet *message);
    Packet * createPacket(short op, std::vector<char> &bytesArr);
    string popString(int start, int end,std::vector<char> &byte);

    //bool ConnectionHandler::operator!=(const ConnectionHandler &rhs) const;
    void send(Packet* cmd);
    void setCurrDataName(std::string currDataName);
    ConnectionHandler(const ConnectionHandler& other);
    void clientProcess(Packet* message);

    ConnectionHandler(std::string host, short port);
    virtual ~ConnectionHandler();
    string superPopString(int start, int end,std::vector<char> byte);
    // Connect to the remote machine
    bool connect();

    // Read a fixed number of bytes from the server - blocking.
    // Returns false in case the connection is closed before bytesToRead bytes can be read.
    bool getBytes(char bytes[], unsigned int bytesToRead);

    // Send a fixed number of bytes from the client - blocking.
    // Returns false in case the connection is closed before all the data is sent.
    bool sendBytes(const char bytes[], int bytesToWrite);

    // Close down the connection properly.
    void close();

    void senddata();

    bool dataCheck(int count);

    vector<char> encodetosend(string &line);
}; //class ConnectionHandler
#endif //CLIENT_CONNECTIONHANDLER_H
