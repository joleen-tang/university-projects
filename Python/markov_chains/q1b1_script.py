#28796527 Joleen Tang
#Copied from q1a_script and edited
import numpy as np
import statistics as st

def transition(current, n, p):
    #n=length of state
    pair=np.random.permutation(n)[:2]
    #Currentx = True if current[pair[x]] is currently happy
    current0=False
    current1=False
    #swapx = True if current[pair[x]] would be happy after swapping
    swap0=False
    swap1=False
    if pair[0]==n-1:    #Avoid indexing error
        if current[pair[0]-1]==current[pair[0]] or current[0]==current[pair[0]]:
            current0=True
        if current[pair[0]-1]==current[pair[1]] or current[0]==current[pair[1]]:
            swap1=True
    else:
        if current[pair[0]-1]==current[pair[0]] or current[pair[0]+1]==current[pair[0]]:
            current0=True
        if current[pair[0] - 1] == current[pair[1]] or current[pair[0] + 1] == current[pair[1]]:
            swap1 = True
    if pair[1]==n-1:
        if current[pair[1] - 1] == current[pair[1]] or current[0] == current[pair[1]]:
            current1 = True
        if current[pair[1] - 1] == current[pair[0]] or current[0] == current[pair[0]]:
            swap0 = True
    else:
        if current[pair[1] - 1] == current[pair[1]] or current[pair[1] + 1] == current[pair[1]]:
            current1 = True
        if current[pair[1] - 1] == current[pair[0]] or current[pair[1] + 1] == current[pair[0]]:
            swap0 = True
    swap=False
    #If agent0 currently unhappy
    if not current0:
        #If a0 would be happy after swap
        if swap0:
            #If a1 would also be happy after swap
            if swap1:
                swap=True
            else:
                #If a1 would be unhappy after swap but is already unhappy
                if not current1:
                    swap=True
        else:
            #if a0 is currently unhappy and would be unhappy after swap
            if swap1 and not current1:
                #If a1 benefits, still swap
                swap=True
    else:
        if swap0 and swap1 and not current1:
            swap=True
    if swap:
        if np.random.random()>=p:
            current[pair[0]], current[pair[1]]=current[pair[1]], current[pair[0]]
    else:
        if np.random.random()<p:
            current[pair[0]], current[pair[1]] = current[pair[1]], current[pair[0]]
    return current

def MonteCarlo(n, p):
    temp=[0 for i in range(6)]
    if n%2==0:
        n1=n//2
    else:
        n1=(n//2)+1
    base=[0 for i in range(n-n1)]+[1 for i in range(n1)]
    for i in range(1000):
        #Current = state of system
        current=np.random.permutation(base)
        for i in range(100):
            current=transition(current, n, p)
        string=str(current[0])+str(current[1])+str(current[2])+str(current[3])
        if string=="1100":
            temp[0]+=1
        elif string=="1010":
            temp[1]+=1
        elif string=="1001":
            temp[2]+=1
        elif string=="0110":
            temp[3]+=1
        elif string=="0101":
            temp[4]+=1
        else:
            temp[5]+=1
    tempsum=sum(temp)
    for i in range(len(temp)):
        temp[i]=temp[i]/tempsum
    return (n, temp)

def Numericaln4(Q4):
    lambda_, v = np.linalg.eig(Q4.T)
    print("The numerically calculated stationary distribution is: "+str(v[:,0]/sum(v[:,0])))
    return

def buildQ4(p):
    # Q4 is constructed under the assumption that there is n//2 or n//2+1 type 1s;
    # States with more or less are not included
    Q4 = np.array([[1 - (4 * p / 6), p / 6, p / 6, p / 6, p / 6, 0],
                   [(1-p) / 6, 1 - (4 *(1-p) / 6), (1-p) / 6, (1-p) / 6, 0, (1-p) / 6],
                   [p / 6, p / 6, 1 - (4 * p / 6), 0, p / 6, p / 6],
                   [p / 6, p / 6, 0, 1 - (4 * p / 6), p / 6, p / 6],
                   [(1-p) / 6, 0, (1-p) / 6, (1-p) / 6, 1 - (4 * (1-p) / 6), (1-p) / 6],
                   [0, p / 6, p / 6, p / 6, p / 6, 1 - (4 * p / 6)]])
    return Q4

Q4=buildQ4(0.3)
temp=MonteCarlo(4, 0.3)
print("For n="+str(temp[0])+", the stationary distribution is "+str(temp[1]))
Numericaln4(Q4)