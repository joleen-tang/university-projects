import sys

class ISTNode:
    def __init__(self, link, edge, parent):
        #Each child is a link to another ISTNode
        self.children=[]
        #Suffix link to another ISTNode
        self.link=link
        #j, i pair indicating substring indices
        self.edge=edge
        #Link to parent node for backtracking
        self.parent=parent

def expTraversal(string, root, l, k, global_end):
    #l is active length
    #k is position of target character
    current = root
    ext1=False
    ext3 = False
    ext2a = False
    ext2b=False
    #While active length is not 0
    while not ext3 and not ext1 and not ext2a and not ext2b and l>0:
        #For each branch from current node
        for child in current.children:
            #Convert global end if necessary
            if child.edge[1] == 'global_end':
                end = global_end
            else:
                end = child.edge[1]
            #If first character matches
            if string[child.edge[0]] == string[k]:
                k += 1
                l -= 1
                #If no more charaters match and end node has no children, case 1
                if l == 0:
                    if k==end and child.children==[]:
                        ext1 = True
                    else:
                        ext3=True
                else:
                    # Compare with each letter in edge
                    for letter in string[child.edge[0]:(end + 1)]:
                        # If mismatch, return child and split it later
                        if letter != string[k]:
                            ext2a = True
                            break
                        else:
                            # If matched
                            l -= 1
                            k+=1
                            # if whole string found (case 1 or 3)
                            if l == 0:
                                if k==end and child.children==[]:
                                    ext1=True
                                else:
                                    ext3 = True
                                break
                current = child
                break
        if not ext1 and not ext2a and not ext3:
            #If no matching paths in children, case 2b
            ext2b=True
    #Current will give the end node for all paths unless ext2b
    return [l, k, current, ext2a, ext2b, ext3, ext1]

def BuildISTree(string):
    #Initialize IST(0)
    root=ISTNode(None, None, None)
    global_end=0
    #Create leaf node
    root.children.append(ISTNode(None, [0, 'global_end'], root))
    #prf=leaf node where prefix ends
    prf=root.children[0]
    lastj=0
    #For each prefix of string
    for i in range(1, len(string)):
        current=prf.parent
        #Pointer to internal node for creating suffix links
        #Phase i+1
        #Extension 1 implicity handled
        global_end+=1
        prevIN=None
        j=lastj
        ext3=False
        print(i, j)
        #For each suffix of prefix
        while j<i and not ext3:
            ext1=False
            ext2a=None
            ext2b=False
            #l is length of substring [j...i]
            l=i-j+1
            #k is position of current character in traversal
            k=j
            #If there is a suffix link
            if current.link is not None:
                #Follow suffix link
                current = current.link
                if current.edge[1] == 'global_end':
                    end = global_end
                else:
                    end = current.edge[1]
                k=end+1
                l=i-k
                # Traverse until end of [k...i] is found
                while l > 0:
                    for child in current.children:
                        ext2b=False
                        if child.edge[1]=='global_end':
                            end=global_end
                        else:
                            end=child.edge[1]
                        # If character is found;
                        #current stops on end node
                        if string[child.edge[0]] == string[k]:
                            k += end - child.edge[0] + 1
                            l -= end - child.edge[0] + 1
                            current = child
                            break
                        else:
                            #If case 2b
                            ext2b=True
                    if ext2b:
                        break
                        #Current is now node before mismatch
                if l==0:
                    #Case 1
                    ext1=True
                    #Current points to leaf node
                elif l<0:
                    child=current
                    #Either ext3 or ext2a
                    current=current.parent
                    if child.edge[1] == 'global_end':
                        end = global_end
                    else:
                        end = child.edge[1]
                    k -= (end - child.edge[0] + 1)
                    l += (end - child.edge[0] + 1)
                    temp=expTraversal(string, current, l, k, global_end)
                    if temp[5]:
                        ext3=True
                        lastj=j
                        break
                    else:
                        ext2a=k
                    current=temp[2]
                else:   #l>0
                    ext2b=True
            else:
                #Returns l, k, current, ext2a, ext2b, ext3, ext1
                temp=expTraversal(string, root, l, k, global_end)
                print(temp)
                if temp[5]: #If whole pattern was found
                    if k==i:    #If it ended on a leaf
                        ext1=True
                    else:
                        ext3=True
                elif temp[4]:   #If there was no path
                    ext2b=True
                elif temp[3]:   #If there was a mismatch at position k, in an edge
                    ext2a=temp[1]
                current=temp[2] #Current is either the node which needs a new leaf (case 2b) or the end node
            #current is the end node of the substring [j...i] (even for case 2a)
            #Case 2b, the node is the one right before the mismatch
            #If extension rule 1, already implicitly done
            #if extention 3, end phase prematurely
            if ext3:
                lastj=j
                break
            # extension 2a; split edge and create new leaf node
            elif ext2a is not None:
                #Create new leaf with rest of current edge
                leaf=ISTNode(None, [ext2a, current.edge[1]], current)
                current.children.append(leaf)
                current.edge[1]=ext2a-1
                #Create new leaf with new suffix
                leaf=ISTNode(None, [ext2a, 'global_end'], current)
                current.children.append(leaf)
                #Adding suffix link
                if prevIN is not None:
                    prevIN.link=current
                prevIN=current
            #extension 2b; node between i and x already exists
            elif ext2b:
                #If current is an internal node
                if current!=root:
                    if current.edge[1] == 'global_end':
                        end = global_end
                    else:
                        end = current.edge[1]
                    leaf=ISTNode(None, [end+1, 'global_end'], current)
                    current.children.append(leaf)
                    # Adding suffix link
                    if prevIN is not None:
                        prevIN.link = current
                    prevIN=current
                else:   #if current is root, create new leaf
                    leaf=ISTNode(None, [j, 'global_end'], current)
                    current.children.append(leaf)
            print(global_end, j)
            print(ext1, ext2a, ext2b, ext3)
            j+=1
    return root

def prob1(stringFile, pairsFile):
    s=open(stringFile, 'r+')
    string=""
    for line in s:
        string=string+str(line)
    p=open(pairsFile, "r+")
    pairs=[]
    for line in p:
        line=line.split()
        pair=[int(line[0]), int(line[1])]
        pairs.append(pair)
    #Something wrong with tree, can't figure it out
    #Assuming it did work:
    root=BuildISTree(string)
    LIJ_print=[]
    for pair in pairs:
        current=root
        LIJ=0
        l=min(len(string)-pair[0], len(string)-pair[1])
        k=pair[0]
        m=pair[1]
        #Until the end of the suffix is reached
        while k<len(string) and m<len(string):
            for child in current.children:
                if child.edge[1] == 'global_end':
                    end = len(string)-1
                else:
                    end = child.edge[1]
                #If there is a match (to both)
                if child.edge[0]==string[k] and child.edge[0]==string[m]:
                    #Jump down to next node
                    if end-child.edge[0]<=l:
                        current=child
                        l-=(end-child.edge[0])
                        k+=(end-child.edge[0])
                        m+=(end-child.edge[0])
                        LIJ+=(end-child.edge[0])
                        break
                    else:   #If there was a match but one of the suffixes ends mid-edge
                        LIJ+=l
                        k=len(string)
                        break
                #Or if there is a match for one
                elif child.edge[0]==string[k] or child.edge[0]==string[m]:
                    #Leave immediately
                    k=len(string)
                    break
        LIJ_print.append([pair[0], pair[1], LIJ])
    f=open("output_lcps.txt", "w+")
    for pair in LIJ_print:
        f.write(LIJ_print[0]+"\t"+LIJ_print[1]+"\t"+LIJ_print[2]+"\n")

prob1(sys.argv[0], sys.argv[1])