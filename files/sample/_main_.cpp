#include<cstdlib>
#include<cmath>
#include<Windows.h>
#include<cstdlib>
#include<cmath>
#include<Windows.h>
#include <iostream>

using namespace std;
int main() {

	freopen("i","r", stdin);
	freopen("o.txt","w", stdout);

    int a, b;
    int t;
    cin >> t;
    while(t--){
        cin >> a >> b;
        cout << a + b  << endl;
    }
    
    return 0;
}
