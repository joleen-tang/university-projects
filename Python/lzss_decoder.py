#Joleen Tang 28796527
import sys
from operator import attrgetter
from bitstring import BitArray

class HuffTreeNode:
    def __init__(self, char, edge, length):
        self.char=char  #Character for node; None if internal
        self.edge=edge  #Edge from parent to node
        self.length=length  #Length of given Huffman code
        self.children=[]

def revElias(string, start):
    #Convert Elias string into int
    current=start   #Current pos in string
    currentLength=1 #Current length of next component
    while string[current]!="1": #While still looking at length components
        temp=current
        current+=currentLength
        currentLength=int("1"+string[temp+1:current], 2)+1
    return [int(string[current:current+currentLength], 2), current+currentLength]

def revHuff(enc):
    #Reverse engineer Huffman tree with header information
    temp=revElias(enc, 0)
    current=temp[1]
    HuffTree=[]
    #Recreate all tree nodes with full edges
    for i in range(temp[0]):
        new=HuffTreeNode(None, None, 0)
        #Interpret 8-bit ASCII character
        new.char=chr(int("0b"+enc[current:current+8], 2))
        hTemp=revElias(enc, current+8)
        current=hTemp[1]
        #Record full encoding as edge label
        new.edge=enc[current:current+hTemp[0]]
        new.length=len(new.edge)
        HuffTree.append(new)
        current+=hTemp[0]
    while len(HuffTree)>1:
        # Sort by alphabetical order (ascending) and then length (descending)
        # Inbuilt sorted function is stable
        HuffTree = sorted(HuffTree, key=attrgetter("edge"))
        HuffTree = sorted(HuffTree, key=attrgetter("length"), reverse=True)
        #New internal node with largest common prefix of two children as edge label, length is length of child's code-1
        new=HuffTreeNode(None, HuffTree[0].edge[0:HuffTree[0].length-1], HuffTree[0].length-1)
        #Change edge labels of children to 0 and 1 respectively (already sorted alphabetically)
        HuffTree[0].edge=0
        HuffTree[1].edge =1
        new.children.append(HuffTree[0])
        new.children.append(HuffTree[1])
        if len(HuffTree)>2:
            HuffTree=HuffTree[2:]
            HuffTree.append(new)
        else:
            HuffTree=[new]
    return [HuffTree[0], current]  #Return root node of reconstructuted Huffman tree and current pos in encoded string

def lzss_decode(encFile):
    enc = BitArray(bytes=open(encFile, "r+b").read()).bin
    temp=revHuff(enc)
    root=temp[0]
    current=temp[1] #Should be pointing to first char of data component
    outputstring=""
    temp=revElias(enc, current)
    current=temp[1]
    num=temp[0]
    for i in range(num):
        if enc[current]=="0":
            temp2=revElias(enc, current+1)
            offset=temp2[0]
            temp2=revElias(enc, temp2[1])
            length=temp2[0]
            for j in range(length):
                outputstring+=outputstring[len(outputstring)-offset]
            current=temp2[1]    #update pointer in encoded string
        else:
            current+=1
            huffNode=root
            while huffNode.char is None:
                huffNode=huffNode.children[int(enc[current])]
                current+=1
            outputstring+=huffNode.char
    f=open("output_lzss_decoder.txt", "w+")
    f.write(outputstring)
    f.close()
    return

lzss_decode(sys.argv[0])