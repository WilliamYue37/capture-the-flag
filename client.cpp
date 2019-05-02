#include <iostream>
#include <winsock2.h>
#include <vector>
 
using namespace std;

const int MAX_SZ = 1e4;

vector<string> split(string s, string delimiter) {
    vector<string> ret;
    long long pos = 0;
    while ((pos = s.find(delimiter)) != string::npos) {
        ret.push_back(s.substr(0, pos));
        s.erase(0, pos + delimiter.length());
    }
    ret.push_back(s);
    return ret;
}

int player;
int r, c, s, angle;
 
int main()
{
    WSADATA WSAData;
    SOCKET server;
    SOCKADDR_IN addr;
 
    WSAStartup(MAKEWORD(2,0), &WSAData);
    server = socket(AF_INET, SOCK_STREAM, 0);
 
    addr.sin_addr.s_addr = inet_addr("10.95.212.236"); // replace the ip with your future server ip address. If server AND client are running on the same computer, you can use the local ip 127.0.0.1
    addr.sin_family = AF_INET;
    addr.sin_port = htons(5555);
 
    connect(server, (SOCKADDR *)&addr, sizeof(addr));

    char buffer[100];
    string temp;
    //get player ID from server
    recv(server, buffer, sizeof(buffer), 0);
    player = buffer[0] - '0';
    cout << "player " << player << endl;

    while (true) {
        //recieve updates from server and update current player state
        memset(buffer, 0, sizeof(buffer));
        recv(server, buffer, sizeof(buffer), 0);

        //send new game state to Java game class
        cout << buffer << endl;

        //recieve new player data from Java game class
        cin >> r >> c >> s >> angle;

        //send new player state to server
        memset(buffer, 0, sizeof(buffer));
        temp = to_string(r) + " " + to_string(c) + " " + to_string(s) + " " + to_string(angle);
        strcpy(buffer, temp.c_str());
        send(server, buffer, sizeof(buffer), 0);
    }

    WSACleanup();
    cout << "Socket closed." << endl;
}