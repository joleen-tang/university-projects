def binString(string):
    string=binString.split()
    for char in string:
        char=int(char)

#Given a string denoting a number in binary
def DCMult(string):
    n=len(string)
    if n==1:
        return string
    d=n//2
    u=string[:d]