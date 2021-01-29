import sys

class BHNode:
    def __init__(self, key, degree, parent, leftchild, rightsib, leftsib):
        self.key=key
        self.degree=degree
        self.parent=parent
        self.leftchild=leftchild
        self.rightsib=rightsib
        self.leftsib=leftsib

class BHHeap:
    def __init__(self, head, Hdegree):
        self.head=head
        self.Hdegree=Hdegree

    def merge(self, t1, t2):
        #Merge 2 binomial trees together
        #t1 and t2 are roots of 2 binomial trees of the same size
        root=None
        #If t1 is smaller
        if t1.key<t2.key:
            #Set t2 parent to t1
            t2.parent=t1
            #Get rid of child tree (t2)'s siblings in current level
            if t2.leftsib!=None:
                t2.leftsib.rightsib=t2.rightsib
            else:   #If t2 is the head
                self.head=t1
            if t2.rightsib!=None:
                t2.rightsib.leftsib=t2.leftsib
            #Get new siblings
            t2.rightsib=t1.leftchild
            t2.leftsib=None
            if t1.leftchild!=None:
                t1.leftchild.leftsib=t2
            t1.degree+=1
            t1.leftchild=t2
            t0=t1
            root=0
        else:
            t1.parent=t2
            #Get rid of child trees siblings in current level
            if t1.leftsib!=None:
                t1.leftsib.rightsib=t1.rightsib
            else:   #If t1 is the head
                self.head=t2
            if t1.rightsib!=None:
                t1.rightsib.leftsib=t1.leftsib
            #Get new siblings
            t1.rightsib=t2.leftchild
            if t2.leftchild!=None:
                t2.leftchild.leftsib=t1
            t2.degree+=1
            t1.rightsib=t2.leftchild
            t1.leftsib=None
            if t2.leftchild!=None:
                t2.leftchild.leftsib=t1
            t2.leftchild=t1
            t0=t2
            root=1
        #Returns root node of merged trees
        #If t1 is smaller, root is 0
        return t0

    def upkeep(self):
        dArray=[-1]*(self.Hdegree+1)
        current=self.head
        merged=False
        #Go through whole linked list
        while current!=None:
            #If array is empty at the specified degree, update
            if dArray[current.degree]==-1:
                dArray[current.degree]=current
            else:
                #Merge two nodes
                d=current.degree
                s=self.merge(current, dArray[current.degree])
                dArray[d]=-1
                merged=True
            current=current.rightsib
        return merged

    def insert(self, BHNode):
        #Adds a node in the top level
        BHNode.rightsib=self.head.rightsib
        BHNode.leftsib=self.head
        self.head.rightsib=BHNode
        if BHNode.rightsib!=None:
            BHNode.rightsib.leftsib=BHNode
        merged=self.upkeep()
        while merged:
            merged=self.upkeep()

    def extract_min(self):
        min=self.head.key
        minptr=self.head
        current=self.head.rightsib
        #Find min key, node
        while current!=None:
            if current.key<min:
                minptr=current
                min=current.key
            current=current.rightsib
        if minptr.degree>0:
            #Promote leftmost child
            current=minptr.leftchild
            current.parent=None
            temp=self.head.rightsib
            #Put left most child to right of head
            self.head.rightsib=current
            current.leftsib=self.head
            #Traverse to get to all children of minptr, edit parent status
            for i in range(1, minptr.degree):
                current=current.rightsib
                current.parent=None
            #Current now pointing to rightmost child, connect with former rightsib of head
            current.rightsib=temp
            if temp!=None:
                temp.leftsib=current
        #Remove minptr node
        if minptr==self.head:
            # If head, make rightsib new head, otherwise heap is empty
            if self.head.rightsib!=None:
                minptr.rightsib.leftsib=None
                self.head=minptr.rightsib
        else:   #May have left or right siblings
            if minptr.leftsib!=None:
                minptr.leftsib.rightsib=minptr.rightsib
            if minptr.rightsib!=None:
                minptr.rightsib.leftsib=minptr.leftsib
        merged=self.upkeep()
        while merged:
            merged=self.upkeep()
        return min

def prob2(listFile):
    f=open(listFile, "r+")
    numbers=[]
    for line in f:
        numbers.append(int(line))
    sorted=[]
    head=BHNode(numbers[0], 0, None, None, None, None)
    BH=BHHeap(head, len(numbers))
    for i in range(1, len(numbers)):
        BH.insert(BHNode(numbers[i], 0, None, None, None, None))
    for i in range(len(numbers)):
        sorted.append(BH.extract_min())
    f=open("output_bhsort.txt", "w+")
    for number in sorted:
        f.write(str(number)+"\n")

prob2(sys.argv[0])