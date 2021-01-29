#Joleen Tang 28796527
import sys

def enumerate(N):
    travList=[]
    for i in range(N+1):  #i=number of intermediate nodes
        travList.append([])
        if i==0:
            travList[i].append("1")
        if i==1:
            travList[i].append("011")
        else:
            for j in range(i):
                for k in range(len(travList[i-j-1])):
                    for l in range(len(travList[j])):
                        new="0"+travList[i-j-1][k]+travList[j][l]
                        travList[i].append(new)
    f=open("output_enumerate.txt", "w+")
    t=1
    for num in travList:
        num=sorted(num)
        for line in num:
            f.write(str(t)+"\t"+str(line)+"\n")
            t+=1

enumerate(sys.argv[0])