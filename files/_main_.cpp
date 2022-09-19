#include<cstdlib>
#include<cmath>
#include<Windows.h>
#include<iostream>
#include<Windows.h>

using namespace std;

int main() {

	freopen("i","r", stdin);
	freopen("o.txt","w", stdout);


	Sleep(2000);
	int n;
	cin >> n;
	int* array = new int[n];
	for (int i = 0; i < n; i++) {
		int j;
		cin >> j;
		array[i] = j << 1;
	}
	for (int i = 0; i < n; i++) {
		cout << array[i] << endl;
	} 
	delete[] array; 
	return 0;
}
