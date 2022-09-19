#include <iostream>
#include <cstring>
#include <algorithm>
#include <set>
#include <map>
#include <vector>
#include <string>
#include <cmath>
#include <queue>
#include <fstream>
#include <cstdio>
#define x first 
#define y second 
using namespace std;

typedef long long ll;

typedef pair<int, int> PII;

typedef pair<int, PII> PIII;

const int N = 1e3 + 10;
const int mod = 1e6 + 3;
int k;
int n, m;
int num[35];
ll  c[N][N], ans[N];
void getC(){
	for(int i = 0; i < N; i++){
		c[0][i] = 0;
		c[i][0] = 1;
	}
	for(int i = 1; i < N; i++){
		for(int j = 1; j < N; j++){
			c[i][j] = (c[i - 1][j] + c[i - 1][j - 1]) % mod;
		}
	}
}
void solve(int caseNum){
	cout << "test case" << endl;
	system("pause");
}
int main(){
    int t;
   	t = 1;
   	int cnt = 1;

    while(t--){
        solve(cnt++);
    }
    return 0;
}