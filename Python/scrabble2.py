class tNode:
    def __init__(self, char):
        self.char=char
        self.children=[]

def insertTrie(root, sorted, word):
    node=root
    for char in sorted:
        inTrie=False
        for child in node.children:
            if child.char==char:
                node=child
                inTrie=True
        if not inTrie:
            newNode=tNode(char)
            node.children.append(newNode)
            node=newNode
    node.children.append(word)
    return node

def countSort(l, n):
    countArray=[[] for i in range(26)]
    for word in l:
        countArray[ord(word[n]) - 97].append(word)
    sorted=[]
    for i in range(26):
        for j in range(len(countArray[i])):
            sorted.append(countArray[i][j])
    return sorted

def radixSort(list, length):
    for i in range(length, 0, -1):
        list=countSort(list, i-1)
    return list

def createAnaTrie(dict):
    root = tNode("*")
    maxAna = root
    # For each word, insert into trie
    for i in range(len(dict)):
        n = insertTrie(root, dict[i][0] + "$", dict[i][1])
        if len(n.children)>len(maxAna.children):
            maxAna=n
    return [maxAna, root]

def findAnaCand(string, index, cand):
    #This function finds all combinations of letters from the string (2^k)
    #If no characters left, append string to candidates list
    if index==len(string)-1:
        if string not in cand:
            cand.append(string)
        return cand
    else:
        #Find all candidate strings using the letter indicated by the string index
        cand=findAnaCand(string, index+1, cand)
        #Find all candidate strings not using the letter indicated by string index
        cand=findAnaCand(string.replace(string[index], "", 1), 0, cand)
    return cand

def solve_task1():
    maxAna=AnaTrie[0]
    return radixSort(maxAna.children, len(maxAna.children[0]))

def solve_task2(query):
    #Set current node to root
    node=AnaTrie[1]
    #Sort query
    sortedQuery=str(countSort(query, 0))
    for r in (("[",""),("'",""),(",",""), (" ",""), ("]","")):
        sortedQuery=sortedQuery.replace(*r)
    sortedQuery=sortedQuery+"$"
    for char in sortedQuery:
        found=False
        for child in node.children:
            if child.char==char:
                node=child
                found=True
        #If not found in trie
        if not found:
            return ["n/a"]
    return radixSort(node.children, len(node.children[0]))

def solve_task3(query, letter_num, boost_amount):
    query=countSort(query, 0)
    for r in (("[",""),("'",""),(",",""), (" ",""), ("]","")):
        query=str(query).replace(*r)
    #Find every combination of anagrams of query string
    cand=findAnaCand(query, 0, [])
    candidates=[]
    #For every combination of anagrams, if anagram is found in trie, add all corresponding words to candidates
    for combo in cand:
        x=solve_task2(combo)
        if x!=["n/a"]:
            candidates+=x
    max=["",0]
    #Calculate score for every word in candidates
    for word in candidates:
        score=0
        for i in range(len(word)):
            if i==letter_num-1:
                score=score+get_letter_score(word[i])*boost_amount
            else:
                score=score+get_letter_score(word[i])
        if score>max[1]:
            max=[word, score]
        elif score==max[1]:
            if word<max[0]:
                max=[word, score]
    if max==["",0]:
        return ["n/a",0]
    return max

#Kept separate from task solve_task1() so that trie can be used for future tasks
d=open("Dictionary.txt", "r+")
#Read in words
dict=[]
for word in d:
    word=word.replace("\n","")
    #Sort each word such that its letters are in alphabetical order
    sortedWord=str(countSort(word, 0))
    for r in (("[",""),("'",""),(",",""), (" ",""), ("]","")):
        sortedWord=sortedWord.replace(*r)
    dict.append([sortedWord, word])

AnaTrie=createAnaTrie(dict)

#############################################################################
# WARNING: DO NOT MODIFY ANYTHING BELOW THIS.
# PENALTIES APPLY IF YOU CHANGE ANYTHING AND TESTER FAILS TO MATCH THE OUTPUT
#############################################################################

# this function returns the score of a given letter
def get_letter_score(char):
    return score_list[ord(char)-96]

               
def print_task1(aList):
    string = ", ".join(aList)
    print("\nThe largest group of anagrams:",string)

def print_task2(query,aList):
    string = ", ".join(aList)

    print("\nWords using all letters in the query ("+query+"):",string)

def print_task3(query,score_boost,aList):
    if len(aList) == 0:
        print("\nThe best word for query ("+query+","+score_boost+"):", "List is empty, task not attempted yet?")
    else:
        print("\nThe best word for query ("+query+","+score_boost+"):",str(aList[0])+", "+str(aList[1]))
        
def print_query(query,score_boost):
    unique_letters = ''.join(set(query))
    unique_letters = sorted(unique_letters)
    scores = []
    for letter in unique_letters:
        scores.append(letter+":"+str(get_letter_score(letter)))
    print("Scores of letters in query: ", ", ".join(scores))
    
        
# score_list is an array where the score of a is at index 1, b at index 2 and so on
score_file = open("Scores.txt")
score_list = [0 for x in range(27)]
for line in score_file:
    line = line.strip()
    line = line.split(":")
    score_list[ord(line[0])-96] = int(line[1])


anagrams_list = solve_task1()    
print_task1(anagrams_list)

query = input("\nEnter the query string: ")


while query != "***":
    score_boost = input("\nEnter the score boost: ")
    print_query(query,score_boost)
    
    score_boost_list = score_boost.split(":")
    letter_num =  int(score_boost_list[0])
    boost_amount = int(score_boost_list[1])
    
    results = solve_task2(query)
    print_task2(query,results)

    answer = solve_task3(query, letter_num, boost_amount)
    print_task3(query,score_boost,answer)
    
    query = input("\nEnter the query string: ")

print("See ya!")
