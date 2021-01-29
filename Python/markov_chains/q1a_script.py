#28796527 Joleen Tang
import numpy as np
import statistics as st

def transition(current, n):
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
        current[pair[0]], current[pair[1]]=current[pair[1]], current[pair[0]]
    return current

def absorbed(current):
    n=len(current)
    for i in range(n-1):
        if current[i-1]!=current[i] and current[i+1]!=current[i]:
            #If there is an agent with no same neighbours
            return False
    if current[n-2]!=current[n-1] and current[0]!=current[n-1]:
        return False
    return True

def MonteCarlo(n):
    absTime=[]
    if n%2==0:
        n1=n//2
    else:
        n1=(n//2)+1
    base=[0 for i in range(n-n1)]+[1 for i in range(n1)]
    for i in range(1000):
        abs=False
        current=np.random.permutation(base)
        #Make sure starting point is not already absorbed
        while absorbed(current):
            current=np.random.permutation(base)
        absT=0
        while not abs:
            current=transition(current, n)
            abs=absorbed(current)
            absT+=1
        absTime.append(absT)
    return (n, st.mean(absTime))

for i in range(4, 11):
    temp=MonteCarlo(i)
    print("For n="+str(temp[0])+", the average absorbing time is "+str(temp[1]))

Q4=np.array([[2/6, 0], [0, 2/6]])
N4=np.linalg.inv(np.identity(2)-Q4)


print("Numerically, absorption time for n=4 is: "+str(N4.dot(np.array([1, 1]))))

#transition to transition part of the canonical form of the transiton matrix for n=5:
Q5=np.array([[4/10, 1/10, 0, 0, 1/10], [1/10, 4/10, 1/10, 0, 0], [0, 1/10, 4/10, 1/10, 0],
             [0, 0, 1/10, 4/10, 1/10], [1/10, 0, 0, 1/10, 4/10]])
N5=np.linalg.inv(np.identity(5)-Q5)

print("Numerically, absorption time for n=5 is: "+str(N5.dot(np.array([1, 1, 1, 1, 1]))))
