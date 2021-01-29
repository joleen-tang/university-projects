import copy

def get_adj_list(filename):
    #O(E)
    file=open(filename)
    edges=[]
    for line in file:
        line=line.strip().split(" ")
        edges.append(line)
    adj_list=[[] for i in range(int(edges[0][0])+1)]
    for i in range(1, len(edges)):
        adj_list[int(edges[i][0])].append([int(edges[i][1]), int(edges[i][2])])
        adj_list[int(edges[i][1])].append([int(edges[i][0]), int(edges[i][2])])
    return adj_list

def insert_min_heap(min_heap, item, vertices, parents, prev):
    #O(log(E))
    #Each item in the array is [vertex, distance]
    i=len(min_heap)
    min_heap.append(item)
    vertices[item[0]]=i
    parents[item[0]]=prev
    sorted=False
    while not sorted and i>0:
        if item[1]<min_heap[i//2][1]:
            #Swap items in min_heap
            min_heap[i], min_heap[i//2]= min_heap[i//2], min_heap[i]
            #Update indicies in vertex list
            vertices[min_heap[i][0]], vertices[min_heap[i//2][0]]=vertices[min_heap[i//2][0]], vertices[min_heap[i][0]]
            i=i//2
        else:
            sorted=True
    return i

def pop_min_heap(min_heap, vertices):
    #O(log(E))
    root=min_heap[0]
    vertices[root[0]]=-1
    min_heap[0]=min_heap[len(min_heap)-1]
    vertices[min_heap[0][0]] = 0
    sorted=False
    i=0
    while not sorted and i<(len(min_heap)//2)-1 and len(min_heap)>1:
       if min_heap[i][1]>min_heap[(2*i)+1][1]:
           min_heap[i], min_heap[(2*i)+1]=min_heap[(2*i)+1], min_heap[i]
           vertices[min_heap[i][0]], vertices[min_heap[(i*2)+1][0]] = vertices[min_heap[(i*2)+1][0]], vertices[min_heap[i][0]]
           i = (2 * i)+1
       elif min_heap[i][1]>min_heap[(2*i)+2][1]:
            min_heap[i], min_heap[(2 * i)+2] = min_heap[(2 * i)+2], min_heap[i]
            vertices[min_heap[i][0]], vertices[min_heap[(i*2)+2][0]]=vertices[min_heap[(i*2)+2][0]], vertices[min_heap[i][0]]
            i = (2 * i) + 2
       else:
           sorted=True
    min_heap.pop()
    return root

def update(min_heap, vertices, vertex, value, prev, parents):
    #O(log(E))
    #Find vertex position in heap and update key
    min_heap[vertices[vertex]]=[vertex, value]
    sorted=False
    i=vertices[vertex]
    parents[vertex]=prev
    #Heap up
    while not sorted and i>0:
        if value<min_heap[i//2][1]:
            #Swap items in min_heap
            min_heap[i], min_heap[i//2]= min_heap[i//2], min_heap[i]
            #Update indicies in vertex list
            vertices[min_heap[i][0]], vertices[min_heap[i//2][0]]=vertices[min_heap[i//2][0]], vertices[min_heap[i][0]]
            i=i//2
        else:
            sorted=True

def task1(source, target, adj_list):
    #Initialize vertices
    vertices=[-2 for i in range(len(adj_list))]
    path=[[], 0]
    finalized=[]
    parents=[-2 for i in range(len(adj_list))]
    #Discovered is a min heap, insert source with distance 0
    discovered=[[source, 0]]
    #Update vertices
    vertices[source]=0
    parents[source]=-1
    while discovered!=[]:
        #Get smallest vertex
        v=pop_min_heap(discovered, vertices)
        #For each edge in adjacency list
        for edge in adj_list[v[0]]:
            #If not discovered
            if vertices[edge[0]]==-2:
                #Insert u, dist
                insert_min_heap(discovered, [edge[0], edge[1]+v[1]], vertices, parents, v[0])
            else:
                #If not finalized
                if vertices[edge[0]]!=-1:
                    if discovered[vertices[edge[0]]][1]>edge[1]+v[1]:
                        update(discovered, vertices, edge[0], edge[1]+v[1], v[0], parents)
        finalized.append(v)
        vertices[v[0]]=-1
        if v[0]==target:
            break
    path[0].append(finalized[-1][0])
    pathFound=False
    #While backtracking path
    current=target
    while current!=source:
        path[0].append(parents[current])
        current = parents[current]
    path[0]=list(reversed(path[0]))
    path[1]=finalized[-1][1]
    return path

def task2(s, t, adj_list):
    C=get_customers("customers.txt")
    #Double adjacency list
    adj_list2=copy.deepcopy(adj_list)
    n=len(adj_list)
    for vertex in adj_list2:
        for edge in vertex:
            edge[0]+=n
    adj_list=adj_list+adj_list2
    #Add weightless edges between customer vertices and customer+length(adj_list) vertices
    for cust in C:
        adj_list[cust].append([cust+n, 0])
        adj_list[cust+n].append([cust, 0])
    path=(task1(s, t+n, adj_list))
    printPath=[[], path[1]]
    for i in range(len(path[0])):
        if path[0][i]<n:
            printPath[0].append(path[0][i])
        else:
            if path[0][i]-n!=path[0][i-1]:
                printPath[0].append(path[0][i]-n)
    return printPath


###################################################
# DO NOT MODIFY THE LINES IN THE BLOCK BELOW.
# YOU CAN WRITE YOUR CODE ABOVE OR BELOW THIS BLOCK
###################################################
def get_customers(filename):
    file = open(filename)
    customers = []
    for line in file:
        line = line.strip()
        customers.append(int(line))
    return customers

# path is a list of vertices on the path, distance is the total length of the path
# task_id must be 1 or 2 (corresponding to the task for which solution is being printed
def print_solution(path,distance,task_id):
    print()
    if task_id == 1:
        print("Shortest path: ", end ="")
    else:
        print("Minimum detour path: ",end="")

    customers = get_customers("customers.txt")

    vertices = []
    for item in path:
        if item in customers:
            vertices.append(str(item)+ "(C)")
        else:
            vertices.append(str(item))

    print(" --> ".join(vertices))
    if task_id == 1:
        print("Shortest distance:", distance)
    else:
        print("Minimum detour distance:",distance)

source = int(input("Enter source vertex: "))
target = int(input("Enter target vertex: "))
####################################################
# DO NOT MODIFY THE LINES IN THE ABOVE BLOCK.
# YOU CAN WRITE YOUR CODE ABOVE OR BELOW THIS BLOCK
###################################################
# Get list adjacency list
adj_list = get_adj_list("edges.txt")
s=(task1(source, target, adj_list))
print_solution(s[0], s[1], 1)
p=(task2(source, target, adj_list))
print_solution(p[0], p[1], 2)