import sys
class ISTNode:
    def __init__(self, start, end, type, link, parent):
        #Assuming alphanumeric alphabet, 62 different charcaters
        self.children=[-1]*62
        self.start=start
        self.end=end
        #If internal node or root, type=-1
        #If leaf, type=j where j is the index where suffix (that ends at leaf) starts
        self.type=type
        self.link=link
        #Pointer to parent
        self.parent=parent

class ISTree:
    def __init__(self, root, string, ptr):
        #An ISTNode with start, end, and type=-1
        self.root=root
        self.string=string
        self.global_end=0
        #Most recently created internal node
        self.currentIN=None
        #Pointer to prefix
        self.ptr=ptr

    def index(self, char):
        # Given a character, give the corresponding index in alphabet arrays used later
        index = ord(char)
        if index > 96:  # If it is a lower case letter
            index -= 61
        elif index > 64:  # If it is an upper case letter
            index -= 55
        else:  # If it is a number
            index -= 48
        return index

    def ext2a(self, node, j, k, l):
        #When the next string in the path is a mismatch and a new node must be created
        #node is the end node where the mismatch occurs between start and end
        #k = mismatched character index (in string)
        #j is the start index of the whole suffix
        #l is the length of the substring in the edge before there was a mismatch; start+l=index of mismatch
        #Create new node from start of edge to char before mismatch
        NP=ISTNode(node.start, node.start+l-1, -1, None, node.parent)
        index=self.index(self.string[node.start])
        node.parent.children[index]=NP  #New parent

        #Update suffix links
        if self.currentIN!=None:
            #Link previous internal node
            self.currentIN.link=NP
        self.currentIN=NP

        #New node from mismatch to leaf
        n=ISTNode(k, self.global_end, j, None, NP)
        index=self.index(self.string[k])
        NP.children[index]=n

        #Update node edge and node.parent child array
        node.start=node.start+l
        node.parent=NP
        index=self.index(self.string[node.start])
        NP.children[index]=node
        return NP

    def ext2b(self, node, j, k):
        #Node between end of matching substring and mismatch
        #node right before mismatch
        #k = mismatched character index
        #j = index of first character of current suffix in string

        #Create new leaf node
        n=ISTNode(k, self.global_end, j, None, node)

        #Add leaf to node.children
        index=self.index(self.string[k])
        node.children[index]=n

        #Update suffix link
        if self.currentIN!=None:
            self.currentIN.link=node
        self.currentIN=node
        return node

    def SCTraversal(self, node, k, l):
        #Skip-count traversal; followed suffix link to 'node'
        #l is how many more characters need to be found
        #k is index of current character in string
        #Returns node (if leaf) or last internal node before end (any other case) after traversal
        current=node
        while l>0:
            index=self.index(self.string[k])
            next=current.children[index]
            if next==-1:  #If there is no branch corresponding to next character, case2b
                #Return node right before mismatch
                return [current, k, l, 2]
            elif (next.end-next.start+1)==l and next.type!=-1:    #If search ends at a leaf, case 1
                #Return leaf node
                return [next, k, l, 1]
            elif (next.end-next.start+1)>=l: #If search would end in an edge, case 2a or case 3
                #Return node before end / mismatch and traverse explicitly from here
                return [current, k, l, 3]
            else:
                l-=(next.end-next.start+1)
                k+=(next.end-next.start+1)
                current=next

    def expTraversal(self, startNode, k, l):
        #For when SCTraversal not available
        #k = index of current character in string
        #l = length of substring being searched for
        currentNode=startNode
        while l>0:
            #Find branch
            index=self.index(self.string[k])
            nextNode=currentNode.children[index]
            if currentNode.children[index]==-1:   #No branch, case 2b
                return [currentNode, k, l, 2]
            currentIndex=nextNode.start
            #Go through each character in edge
            while currentIndex<=nextNode.end:
                #If mismatch
                if self.string[currentIndex]!=self.string[k]:
                    #Case 2a
                    return [nextNode, k, l, 3]
                currentIndex+=1
                k+=1
                l-=1
                if l==0:    #if l==0 and currentIndex<=nextNode.end
                    if nextNode.type!=-1 and currentIndex==nextNode.end:   #If leaf, case 1 and end of edge
                        return [nextNode, k, l, 1]
                    else:   #Not leaf, or not end of edge, case 3
                        return [nextNode, k, l, 0]
            currentNode=nextNode


def prob1(stringFile, pairsFile):
    f=open(stringFile, "r+")
    string=""
    for line in f:
        string=string+str(line)
    string=string+"$"

    #Building suffix tree, insert i=0
    init=ISTNode(0, -1, 0, None, None)
    STree=ISTree(ISTNode(-1, -1, -1, None, -1), string, init)
    STree.ptr.end=STree.global_end
    STree.ptr.parent=STree.root
    #Insert first node in root children
    index=STree.index(string[0])
    #Start at prefix
    STree.root.children[index]=STree.ptr
    #For every prefix
    for i in range(len(string)):
        #Increment global_end
        STree.global_end+=1
        current=STree.ptr
        lastj=0
        j=0
        #For each j from lastj to i+1
        for j in range(lastj+1, STree.global_end):
            #k= first char in edge label from u to end of str[j-1...i]
            k=current.start+1
            l=i-k+1
            if l!=0:
                if current!=STree.root:
                    current=current.parent
                #If root is parent (if u==r)
                if current.parent==-1:
                    stop = STree.expTraversal(STree.root, k, l)
                else:
                    #Follow suffix link to another internal node
                    current=current.link
                    stop=STree.SCTraversal(current, k, l)
                    #stop=[node, k, l, ext type]
                    #if ext type==3, could be either 2a or 3
                    if stop[3]==3:
                        #Traverse explicitly to find which
                        stop=STree.expTraversal(stop[0], stop[1], stop[2])
            else:
                #If [k...i] is empty and current is a leaf node
                if current.type!=-1:
                    stop=[current, k, l, 1]
                else:   #[k...i] is empty and not a leaf node; case 3
                    stop=[current, k, l, 0]
            #Stop[3]==1: case 1
            #stop[3]==2: case 2b
            #Stop[3]==3: case 2a
            #Stop[3]==0: case 3
            if stop[3]==0:
                #If ext3, update last j and end phase prematurely
                lastj=j
                break
            elif stop[3]==2:
                if j==8:
                    print(i, j)
                #If case 2b, apply ext2b
                STree.ext2b(stop[0], j, stop[1])
            elif stop[3]==3:
                #node=[node, k, l, ext type]
                #If case 2a, apply ext2a
                STree.ext2a(stop[0], j, stop[1], stop[2])
            else:
                #Case 1
                stop[0].end=STree.global_end
        #All extensions complete, no early termination
        lastj=j-1

    #STree now a complete, explicit suffix tree
    f=open(pairsFile)
    pairs=[]
    for line in f:
        line=line.split()
        pairs.append([int(line[0]), int(line[1])])
    f=open("output_lcps.txt", "w+")
    for pair in pairs:
        #Indices, -1 to account for python indicies
        k1=pair[0]-1
        k2=pair[1]-1
        #l=length of the shorter suffix
        #j is always larger so suffix from k2 is always shorter
        l=len(string)-k2
        #Length of longest common prefix
        LIJ=0
        current=STree.root
        #While current characters are common across both suffixes and l>0 (end of shorter suffix not found yet)
        while STree.string[k1]==STree.string[k2]:
            index=STree.index(STree.string[k1])
            #Jump to child
            current=current.children[index]
            #Update indices and LIJ
            l-=(current.end-current.start+1)
            k1+=(current.end-current.start+1)
            k2+=(current.end-current.start+1)
            LIJ+=(current.end-current.start+1)
            if l==0:
                break
        if l==0:    #If it reached the end
            LIJ-=1  #Get rid of terminal symbol
        #Add LIJ to pair table; -1 to get rid of terminal symbol
        pair.append(LIJ)
        #+1 to change indices back
        f.write(str(pair[0])+"\t"+str(pair[1])+"\t"+str(pair[2])+"\n")

prob1(sys.argv[0], sys.argv[1])
#I broke the tree somehow and idk how to fix it
#Ukkonen was a smart cookie