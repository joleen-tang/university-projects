#Joleen Tang 28796527
import sys
from operator import attrgetter, itemgetter
from bitstring import Bits

def Elias(int):
    #Encode dec(int) using Elias code
    n=bin(int)
    enc=n[2:]
    l=len(enc)-1
    while l>0:
        n=bin(l)
        #Change leading 1 to 0
        enc='0'+n[3:]+enc
        l=len(n)-3
    return enc

class HuffTreeNode:
    def __init__(self, char, freq, edge):
        self.char=char
        self.freq=freq
        self.edge=edge  #Edge from parent to node
        self.children=[]

def HuffTraversal(current, HuffList, code):
    if current.edge is not None:    #If current is not root node
        code+=str(current.edge)
    if current.children==[]:    #If leaf
        HuffList[ord(current.char)]=code
        return
    else:
        for child in current.children:
            HuffTraversal(child, HuffList, code)
    return HuffList

def Huffman(freqList):
    #Input is a list with charcaters and their frequencies
    #Sort list in ascending order of frequency
    freqList=sorted(freqList, key=itemgetter(1))
    n=len(freqList)
    HuffTree=[]
    for i in range(n):
        #Create nodes for each character (char, freq, edge label)
        HuffTree.append(HuffTreeNode(freqList[i][0], freqList[i][1], None))
    while len(HuffTree)>1:
        #Set new edge labels for nodes with lowest freqs
        HuffTree[0].edge="0"
        n=HuffTree[0]
        HuffTree[1].edge="1"
        m=HuffTree[1]
        #Create new internal node
        new=HuffTreeNode(n.char+m.char, n.freq+m.freq, None)
        new.children.append(n)
        new.children.append(m)
        if len(freqList)>2: #If more merges need to be done
            HuffTree.append(new)
            #Add node to list and sort
            HuffTree=sorted(HuffTree[2:], key=attrgetter("freq"))
        else:   #If there are only two left, they were just merged and the new node is the root
            HuffTree=[new]
    #Create alphabet sized array, store Huffman codes for each character to make lookup easier
    HuffList=[-1]*128
    HuffTraversal(HuffTree[0], HuffList, "")
    #Returns alphabet sized array with either -1 (not found in text) or an encoded string for each char
    return HuffList

def LZSS(text, dSize, buff):
    f=open(text, "r+")
    tempList=[0]*128
    t=""
    #Get frequencies for all characters in text
    for line in f:
        t+=line
        for char in line:
            tempList[ord(char)]+=1
    #Create freq list for each character that appeared at least once
    #In form [character, frequency]
    freqList=[]
    for i in range(len(tempList)):
        if tempList[i]!=0:
            freqList.append([chr(i), tempList[i]])
    HuffList=Huffman(freqList)
    header=Elias(len(freqList))
    for i in range(len(freqList)):
        #Encode ASCII code char
        n=bin(ord(freqList[i][0]))
        n=n[2:]
        #Make it 8 bits; is there a better way to do this?
        if len(n)<8:
            n=("0"*(8-len(n)))+n
        header+=n
        header+=Elias(len(HuffList[ord(freqList[i][0])]))
        header+=HuffList[ord(freqList[i][0])]
    data=""
    num=0   #Number of 0/1 fields
    #Start of buffer window
    start=0
    #Position of start and end of dictionary
    dict=[-1, -1]
    while start<len(t):
        if dict!=[-1, -1]:
            current=dict[0] #Pos of start of string to be matched in dictionary
            length=0
            offset=0
            while current<dict[1]+1:    #While still in range of dictionary
                temp=0
                #While in range of text
                while start+temp<len(t) and temp<buff:
                    #If matched
                    if t[current+temp]==t[start+temp]:
                        temp+=1
                    else:
                        break
                if temp>length:
                    length=temp
                    offset=start-current
                current+=1
            if length<3:    #Encode  1-bit
                data+="1"+HuffList[ord(t[start])]
                start+=1    #Update buffer window
                dict[1]+=1  #Update dictionary
            else:
                data+="0"+Elias(offset)+Elias(length)
                start+=length
                dict[1]+=length
            if dict[1] - dict[0] + 1 > dSize:   #Check if dictionary is appropriate size
                dict[0]=dict[1]-dSize+1
        else:   #If dictionary is empty
            data+="1"+HuffList[ord(t[start])]
            start+=1
            dict=[0, 0]
        num+=1
    data=Elias(num)+data
    return header+data

output=LZSS(sys.argv[0], sys.argv[1], sys.argv[2])
f=open("output_lzss_encoder.bin", "w+b")
Bits("0b"+output).tofile(f)
f.close()

