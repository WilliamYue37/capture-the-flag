#include <iostream>
#include <winsock2.h>
 
using namespace std;

const int MAX_SZ = 1e4;

int player;
int r, c, s;
 
int main()
{
    WSADATA WSAData;
    SOCKET server;
    SOCKADDR_IN addr;
 
    WSAStartup(MAKEWORD(2,0), &WSAData);
    server = socket(AF_INET, SOCK_STREAM, 0);
 
    addr.sin_addr.s_addr = inet_addr("10.95.51.3"); // replace the ip with your future server ip address. If server AND client are running on the same computer, you can use the local ip 127.0.0.1
    addr.sin_family = AF_INET;
    addr.sin_port = htons(5555);
 
    connect(server, (SOCKADDR *)&addr, sizeof(addr));
    cout << "Connected to server!" << endl;
 
    char* buffer;
    char buffer2[100];
    string temp;
    //get player ID from server
    recv(server, buffer, sizeof(buffer), 0);
    player = buffer[0] - '0';
    cout << buffer << endl;
    cout << "You are player #" << player << endl;

    while (true) {
        //recieve updates from server and update current player state
        recv(server, buffer, sizeof(buffer), 0);
        strcpy(buffer2, buffer);
        temp = strtok(buffer2, "|");
        if (player != 1) temp = strtok(NULL, "|");
        r = temp[0];
        c = temp[2];
        s = temp[4];

        //send new game state to Java game class
        cout << r << c << s;

        //recieve new player data from Java game class
        cin >> r >> c >> s;

        //send new player state to server
        temp = to_string(r) + " " + to_string(c) + " " + to_string(s);
        strcpy(buffer, temp.c_str());
        send(server, buffer, sizeof(buffer), 0);
    }

    WSACleanup();
    cout << "Socket closed." << endl;
}