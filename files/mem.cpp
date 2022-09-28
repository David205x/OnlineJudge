#include<iostream>
#include<string>
#include<windows.h>
#include<time.h>
#include<psapi.h>
#pragma comment(lib,"psapi.lib")

using namespace std;

DWORD str2dword(string val) {
    DWORD dword;
    sscanf_s(val.c_str(), "%ul", &dword);
    return dword;
}

double timeElapsed(clock_t& fin, clock_t const& start) {
    fin = clock();
    return double(fin) - double(start);
}

#define SOURCE_CONTROL true

int main(int argc, char *argv[]) {

#if SOURCE_CONTROL

    // argc: 3 | argv: [0]: Path, [1]: PID, [2]: Time limit
    string PID(argv[1]);
    if (PID.empty()) {
        return 2;
    }

    string strTimeLimit(argv[2]);
    if (strTimeLimit.empty()) {
        return 3;
    }

#endif;

#if !SOURCE_CONTROL

    string PID = "7740";
    string strTimeLimit("1000");

#endif;

    DWORD dPID = str2dword(PID);
    double timeLimit = atoi(strTimeLimit.c_str());
    
    // cout << " -- PID: " << PID << " TIME_LIMIT: " << timeLimit << " task executing." << endl;
      
    HANDLE memHandle = OpenProcess(
        PROCESS_QUERY_INFORMATION | PROCESS_VM_READ,
        FALSE, dPID
    );

    PROCESS_MEMORY_COUNTERS pmc;

    clock_t start, cur, finish;

    int memUsagePeak = -1;

    start = clock();
    while (timeElapsed(cur, start) <= timeLimit) {
        GetProcessMemoryInfo(memHandle, &pmc, sizeof(pmc));

        int memUsageSamplePoint = int(pmc.WorkingSetSize) >> 10;
        if (memUsageSamplePoint > memUsagePeak) {
            memUsagePeak = memUsageSamplePoint;
        }

        printf("%.0lf ", double(memUsageSamplePoint));
        Sleep(20);
    }
    printf("\n");
    finish = clock();

    CloseHandle(memHandle);

    if (memUsagePeak < 0) {
        printf("-1\n");
        return 0;
    }

    // cout << "task finished, peak memory: " << memUsagePeak << " kb, " << timeElapsed(finish, start) << "ms elapsed." << endl;

    return 1;
}