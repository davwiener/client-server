//
// 
//

#include "../include/ConnectionHandler.h"
#include <fstream>
#include "../include/LOGRQ.h"
using namespace std;

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

short ConnectionHandler::bytesToShort(char  bytesArr [])
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

short ConnectionHandler::bytes2ShortInTheMiddle(std::vector<char> &byteArr, int startPlace, int endPlace)
{
    char arrClone[2];
    arrClone[0] = byteArr.at(startPlace);
    arrClone[1] = byteArr.at(endPlace);
    short result = (short)((arrClone[0] & 0xff) << 8);
    result += (short)(arrClone[1] & 0xff);
    return result;
}
std::vector<char> ConnectionHandler:: shortToBytes(short num)
{
    std::vector<char> bytesArr(2);
    bytesArr[0] = static_cast<char>((num >> 8) & 0xFF);
    bytesArr[1] = static_cast<char>(num & 0xFF);
    return bytesArr;
}

std::vector<char> ConnectionHandler:: encode(Packet* message)
{
    std::vector<char> sendBytes;
    sendBytes.clear();
    std::vector<char> opcode = shortToBytes(message->getOpCode());
    short op = static_cast<short>(message->getOpCode());

    switch (op)
    {
        case 1:
        { //RRQ
            string name = message->getName();
            sendBytes = std::vector<char>(name.length() + 3);
            sendBytes.push_back( opcode[0]);
            sendBytes.push_back(opcode[1]);
            std::vector<char> stringname (name.begin(), name.end());
            for (unsigned short i = 0; i < name.length(); i++)
            {
                sendBytes[i + 2] = stringname[i];
            }
            sendBytes.push_back( '\0');
        }break;
        case 2:
        { //WRQ
            string name = message->getName();
            sendBytes = std::vector<char>(name.length() + 3);
            sendBytes.push_back(opcode[0]);
            sendBytes.push_back(opcode[1]);
            std::vector<char> stringname(name.begin(), name.end());
            for (unsigned short i = 0; i < name.length(); i++)
            {
                sendBytes[i + 2] = stringname[i];
            }
            sendBytes.push_back('\0');
        }break;
        case 3:
        { //data

            std::vector<char> bData = message->getData();

            long longpacketsize = message->getPacketSize();

            short shortblocknum = message->getBlockNum();
            std::vector<char> packetsize = shortToBytes(longpacketsize);
            std::vector<char> blocknum = shortToBytes(shortblocknum);
            sendBytes.push_back(opcode[0]);
            sendBytes.push_back( opcode[1]);
            sendBytes.push_back(packetsize[0]);
            sendBytes.push_back(packetsize[1]);
            sendBytes.push_back(blocknum[0]) ;
            sendBytes.push_back(blocknum[1]);
            for (long i = 0; i < longpacketsize; i++)
            {
                char ch=bData.at(i);
                sendBytes.push_back(ch);
            }
        }break;
        case 4:
        { //ack

            short shortblocknum = message->getBlockNum();
            std::vector<char> blocknum = shortToBytes(shortblocknum);
            sendBytes.push_back(opcode[0]);
            sendBytes.push_back(opcode[1]);
            sendBytes.push_back(blocknum[0]);
            sendBytes.push_back(blocknum[1]);
        }break;
        case 5:
        { //error
            string s = message->getErrorMessage();
            short errorcode = message->getBlockNum();
            std::vector<char> stringname (s.begin(), s.end());
            sendBytes = vector<char>(5 + stringname.size());
            std::vector<char> errorcodebyte = shortToBytes(errorcode);
            sendBytes.push_back(opcode[0]);
            sendBytes.push_back(opcode[1]);
            char ch=errorcodebyte.at(0);
            sendBytes.push_back(ch);
            ch=errorcodebyte.at(1);
            sendBytes.push_back(ch);
            sendBytes.push_back( 0);
            /*for (short i = 3; i < stringname.size(); i++)
            {
                sendBytes[i] = stringname[i - 3];
            }*/
        }break;
        case 6:
        { //DIRQ
            sendBytes = vector<char>(2);
            sendBytes.push_back(opcode[0]);
            sendBytes.push_back( opcode[1]);
        }break;
        case 7:
        { //LOG
            string name = message->getName();
            sendBytes = std::vector<char>(name.length() + 3);
            sendBytes.push_back(opcode[0]);
            sendBytes.push_back(opcode[1]);
            std::vector<char> stringname(name.begin(), name.end());
            for (unsigned short i = 0; i < name.length(); i++)
            {
                sendBytes.push_back(stringname[i]);
            }
            sendBytes[name.length() + 2] = 0;
        }break;
        case 9:
        { //bcast
            short shortblocknum = message->getBlockNum();
            string s = message->getName();
            std::vector<char> stringname (s.begin(), s.end());
            sendBytes = std::vector<char>(4 + stringname.size());
            std::vector<char> zeroorone = shortToBytes(shortblocknum);
            sendBytes.push_back(opcode[0]);
            sendBytes.push_back(opcode[1]);
            sendBytes.push_back(zeroorone[0]);
            for (unsigned short i = 3; i < stringname.size(); i++)
            {
                sendBytes.push_back(stringname[i - 3]);
            }
            sendBytes.push_back(0);
        }break;
        case 10:
        { //DISC
            sendBytes = vector<char>(2);
            sendBytes.push_back(opcode[0]);
            sendBytes.push_back(opcode[1]);
        }break;
        default:{
            //send ERROR;
        }
    }
    return (sendBytes); //uses utf8 by default
}
bool ConnectionHandler::dataCheck(int count)
{
    if(datasize+6==count)
        return true;
    else
        return false;
}
Packet* ConnectionHandler::createPacket(short op, std::vector<char> &bytesArr)
{
    int e=(int)bytesArr.size();
    char last=bytesArr.at(e-1);
    if(e==4&&op==3){
        datasize=((long)bytes2ShortInTheMiddle(bytesArr,2,3));
    }
    if((last=='\0'&&op!=9&&e>3&&op!=3)||(op==10)||(op==6)||(op==4&&e==4)||(op==9 &&last=='\0'&&e>4)||(op==3&&dataCheck(e)))
    {
        switch (op)
        {
            case 0:
            {

                return NULL;
            }
            case 1:
            {

                string name;
                name = popString(2, e - 1, bytesArr); // Count or count-1 ? xD
                RRQ* p = new RRQ(name);
                return p;
            }
       //break;
            case 2:
            {

                string name;
                name = popString(2, e - 1, bytesArr);
                WRQ* p = new WRQ(name);
                return p;
            }//break;
            case 3:
            {

                int bNum = bytes2ShortInTheMiddle(bytesArr, 4, 5);
                std::vector<char> *dBytes=new vector<char>;
                long dsize=datasize;
                for (int i = 0; i < dsize; i++)
                {
                    char ch=bytesArr.at(i + 6);
                    dBytes->push_back(ch);
                }
                DATA *p =new DATA(dsize, bNum, *dBytes);
                datasize=0;
                return p;
            }//break;
            case 4:
            {
                int bNum = bytes2ShortInTheMiddle(bytesArr, 2, 3);
                ACK* p = new ACK((short) bNum);
                return p;
            }//break;
            case 5:
            {
                int errCode = bytes2ShortInTheMiddle(bytesArr, 2, 3);

                ERROR* p = new ERROR(errCode); // Can add Error msg as well.
            return p;
            }//break;
            case 6:
            {
                DIRQ* p =  new DIRQ();
                return p;
            }//break;
            case 7:
            {
                string name = popString(2,e-1, bytesArr );
                LOGRQ* p = new LOGRQ(name);
                return p;
            }//break;
            case 9:
            {
                int added = bytesArr[2];
                string name = popString(3, e-1, bytesArr); // FIX.
                BCAST* p = new BCAST(name, (short) added);
                return p;
            }//break;
            case 10:
            {
                DISC* p = new DISC();
                return p;
            }//break;
            default:{
                ERROR* p = new ERROR(4);
                return p;
            }
                break;
        }
    }
    else
        return NULL;

}

std::string ConnectionHandler::popString(int s, int e,std::vector<char> &byte)
{
    char *arr =new char[e - s];
    for(int i= s; i < e; i++){
        char d=byte.at(i);
        arr[i-s]=d;
    }
    std::string str(arr);
    delete arr;
    return str;
}
ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_),currdatanum (1),names(false),end(0), start(0),canSend (true),stopsenddata(false),currDataName(""),currentdata(),currOp(){};

ConnectionHandler::~ConnectionHandler() {
    close();
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to "
              << host_ << ":" << port_ << std::endl;
    try {
        tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
        boost::system::error_code error;
        socket_.connect(endpoint, error);
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
            tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if(error)
            throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

void ConnectionHandler::send(Packet *cmd)
{
    currOp= (short) cmd->getOpCode();
    if (currOp==1 || currOp==2)
    {
        currDataName = cmd->getName();
    }
    if (canSend)
    {

        std::vector<char> bytes2send;
        bytes2send.clear();
        bytes2send=encode(cmd);
        char charstosend [bytes2send.size()] ;
        for (unsigned long i=0;i<bytes2send.size();i++)
        {

            char currchar=bytes2send.at(i);
            charstosend[i]=currchar;
        }
        sendBytes(charstosend, (int) bytes2send.size());

        canSend = false;
    }
}

string ConnectionHandler::superPopString(int s, int e,std::vector<char> byte)
{
    std::string str="";
    for(int i=s;i<e-1;i++){
       char c=byte.at(i);
       if (c=='\0')
        {
            c='\n';
        }
        str=str+c;
    }
    return  str;

}

void ConnectionHandler::clientProcess(Packet * message)
{
    int opCode= message->getOpCode();
    if (opCode==9) //BCAST
    {
        if(message->getBlockNum()==1)
        {
            std::cout << string("BCAST") <<" "<<string("add")<<" "<< message->getName()<< std::endl;
        }
        else
        {
            std::cout << string("BCAST") <<" "<<string("del")<<" "<< message->getName()<< std::endl;
        }
    }
    else
    {
        if (currOp==6)//DIRQ
        {
            if (opCode==3)
            {
                if (message->getData().size()<512){

                    currentdata = message->getData();
                    int z= (int) currentdata.size();
                    string superS= superPopString(0,z,currentdata);
                    cout <<superS<<endl;
                    canSend = true;
                    currdatanum=1;
                    currentdata.clear();
                }
                else//if there are more then 512 bytes
                {
                    names=true;
                    std::vector<char> tempnames=message->getData();
                    int namesize=tempnames.size();
                    for(int i=0;i<namesize;i++){
                        char ch=tempnames.at(i);
                        currentdata.push_back(ch);
                    }

                    canSend = true;
                    ACK *a=new ACK(currdatanum);
                    send(a);
                    currdatanum++;
                }
            }
            else
            {
                canSend = true;
                ERROR *err=new ERROR(0);
                send(err);
            }
        }
        else
        {
            if (currOp==10) //DISC
            {
                if (opCode==4){ //ACK
                    std::cout << "ACK " << message->getBlockNum() <<std::endl;
                    //can close
                    close();
                } else {
                    canSend = true;
                    ERROR *err=new ERROR(0);
                    send(err);
                }
            }
            else
            {
                if (currOp==1||currOp==4)  //RRQ        // we asked for RRQ
                {
                    if (opCode==3)//DATA      // we need get DATA
                    {

                        long pSize=message->getPacketSize();
                        if (pSize < 512)
                        {
                            long newdsize=message->getPacketSize();
                            std::vector<char> currdata=message->getData();
                            for (long i = 0 ; i < newdsize;i++)
                            {
                                char ch=currdata.at(i);
                                currentdata.push_back(ch);
                            }

                            // don't know if this works.
                            ofstream FILE;
                            char *cstr = new char[currDataName.length() + 1];
                            strcpy(cstr, currDataName.c_str());
                            ofstream fout(cstr, ios::out | ios::binary);
                            char dataArray[currentdata.size()];
                            for(unsigned long j=0;j<currentdata.size();j++)
                                dataArray[j]=currentdata.at(j);
                            fout.write(dataArray,currentdata.size());
                            fout.close();
                            if(!names){
                                cout<<"RRQ"<<" "<<currDataName<<" "<<"complete"<<endl;
                            }
                            else{
                                long z=currentdata.size();
                                string superS= superPopString(0,z, currentdata);
                                cout <<superS<<endl;
                                canSend = true;
                            }
                            names=false;
                            currentdata.clear();
                            currDataName = "";

                        }
                        else
                        {

                            long newdsize=message->getPacketSize();
                            std::vector<char> currdata=message->getData();
                            for (long i = 0 ; i < newdsize;i++)
                            {
                                char ch=currdata.at(i);
                                currentdata.push_back(ch);
                            }
                            canSend = true;
                            ACK *a=new ACK(message->getBlockNum());
                            send(a);
                        }
                    }
                    else
                    {
                        if (opCode==5)//EROR
                        {

                            message->printError();
                            canSend = true;
                        }
                    }
                }
                else
                {
                    if (currOp==2 || currOp==3) //2=WRQ 3=DATA        // we asked WRQ or DATA (from the WRQ)
                    {
                        if (opCode==4)  //ACK
                        {

                            canSend = true;
                            std::cout << "ACK " << message->getBlockNum() <<std::endl;
                            if(currOp==2)
                            {
                                vector<char> vec;
                                fstream myfile;
                                myfile.open(currDataName, ios::in);
                                myfile.seekg(0, myfile.end);
                                datasize = (unsigned long)myfile.tellg();
                                vec.resize(datasize);
                                myfile.seekg(0, std::ios_base::beg);
                                myfile.read(&vec[0], datasize);
                                for(long i=0;i<datasize;i++){
                                   char ch=vec.at(i);
                                    currentdata.push_back(ch);
                                }
                                myfile.close();
                                start =0;
                                end= 512;
                                senddata();
                            }
                            else//if its not the first send
                            {
                                if(!stopsenddata){

                                    start =end;
                                    end=start+512;
                                    if(end>datasize)
                                        end=datasize;
                                    currdatanum++;
                                    senddata();
                                }
                                else{
                                    stopsenddata=false;
                                }
                            }
                        }
                        else                                // if we dont have ACK, we have ERROR.
                        {
                            message->printError();
                            currDataName="";//make the name empty
                        }

                    }
                    else                                    // we asked for something else like LOGRQ. options: ACK or ERROR.
                    {
                        if (opCode==4)//ACK
                        {

                            std::cout << "ACK " << message->getBlockNum() <<std::endl;
                            canSend = true;
                        }
                        else
                        { //error

                            message->printError();
                            canSend = true;
                            //print error.
                        }
                        // CAN SEND DATA
                    }
                }
            }
        }
    }
}
void ConnectionHandler::senddata()
{

    int size=end-start;
    bool last=false;
    if (size < 512) {
        last=true;

    }

    std::vector<char> *newdata=new vector<char>;
    for (long i = start ; i < end;i++)
    {
        char c=currentdata.at(i);
        newdata->push_back(c);
    }
    DATA *d1;
    d1 = new DATA(size, currdatanum, *newdata);
    send(d1);
    if(last){
        currdatanum = 1;
        end=0;
        start=0;
        currentdata.clear();
        stopsenddata=true;
        cout<<"WRQ"<<" "<<currDataName<<" "<<"complete"<<endl;
        currDataName="";
    }



}
std::vector<char> ConnectionHandler:: encodetosend(string& line){
    vector <char> ans;
    std::string packet=line.substr(0,line.find(" "));

    if(packet=="LOGRQ")
    {
        ans.push_back(0);
        ans.push_back(7);
        for (unsigned int i=packet.length()+1;i<line.size();i++)
        {
            char ch=line.at(i);
            ans.push_back(ch);
        }
        ans.push_back('\0');
        currOp=7;
    }
    if(packet=="DELRQ")
    {
        ans.push_back(0);
        ans.push_back(8);
        for (unsigned int i=packet.length()+1;i<line.size();i++)
        {
            char ch=line.at(i);
            ans.push_back(ch);
        }
        ans.push_back('\0');
        currOp=8;
    }
    if(packet=="WRQ")
    {
        ans.push_back(0);
        ans.push_back(2);
        for (unsigned int i=packet.length()+1;i<line.size();i++)
        {
            char ch=line.at(i);
            ans.push_back(ch);
        }
        ans.push_back('\0');
        currDataName=line.substr(line.find(" ")+1,line.size()-1);
        currOp=2;
    }
    if(packet=="RRQ")
    {
        ans.push_back(0);
        ans.push_back(1);
        for (unsigned int i=packet.length()+1;i<line.size();i++)
        {
            char ch=line.at(i);
            ans.push_back(ch);
        }
        ans.push_back('\0');
        currDataName=line.substr(line.find(" ")+1,line.size()-1);
        currOp=1;
    }
    if(packet=="DISC")
    {
        ans.push_back(0);
        ans.push_back(10);
        currOp=10;
    }
    if(packet=="DIRQ")
    {
        ans.push_back(0);
        ans.push_back(6);
        currOp=6;
    }
    return ans;
}
void ConnectionHandler::setCurrDataName(std::string currDataName) {
    ConnectionHandler::currDataName = currDataName;
}

ConnectionHandler::ConnectionHandler(const ConnectionHandler& other): host_(other.host_), port_(other.port_), io_service_(),socket_(io_service_) ,
                                                                      currdatanum (other.currdatanum),names(other.names),end(other.end), start(other.start),
                                                                      canSend (other.canSend),stopsenddata(other.stopsenddata),currDataName(other.currDataName),currentdata(other.currentdata),currOp(other.currOp )
 {}

bool ConnectionHandler::operator==(const ConnectionHandler &rhs) const {
    return host_ == rhs.host_ &&
           port_ == rhs.port_ &&
           currdatanum == rhs.currdatanum &&
           names == rhs.names &&
           end == rhs.end &&
           start == rhs.start &&
           datasize == rhs.datasize &&
           canSend == rhs.canSend &&
           stopsenddata == rhs.stopsenddata &&
           currDataName == rhs.currDataName &&
           currentdata == rhs.currentdata &&
           currOp == rhs.currOp;
}

bool ConnectionHandler::operator!=(const ConnectionHandler &rhs) const {
    return !(rhs == *this);
}



