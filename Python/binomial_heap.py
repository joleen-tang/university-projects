class Binomial_tree_node:
    def __init__(self, key, value, degree, parent, leftchild, rightsibling):
        self.key=key
        self.value=value
        self.degree=degree
        self.parent=parent
        self.leftchild=leftchild
        self.rightsibling=rightsibling

class binomial_heap:
    def __init__(self):
        self.head=None
        return

    def insertNewHeap(self, key, value):
        #Create and insert new B(0) heap
        if self.head is None:
            self.head=(key, value, 0, None, None, None)
        else:
            self.head.rightsibling=Binomial_tree_node(key, value, 0, None, None, self.head.rightsibling)
        #Return newly created heap
        return self.head.rightsibling

    def merge(self, head1, head2):
        #If root of h1 is smaller than h2
        if head1.key<head2.key:
            #Set right sibling of head of h2 to be h1's head's left child
            head2.rightsibling=head1.leftchild
            #Set h1's head's left child to be h2's head
            head1.leftchild=head2
            #Update h1's head degree
            head1.degree+=1
        else:
            #Set right sibling of head of h1 to be h2's head's left child
            head1.rightsibling=head2.leftchild
            #Set h2's head's left child to be h1's head
            head2.leftchild=head1
            #Update h1's head degree
            head2.degree+=1

    def insertNewElement(self, key, value):
        n=self.insertNewHeap(key, value)
        self.merge(self)