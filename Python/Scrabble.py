#Task 1
def countSort(list, index):
    countList=[[] for i in range(26)]
    #Count occurrences of letters
    for word in list:
        countList[ord(word[index])-97].append(word)
    sortedList=[]
    #Add sorted words into return list
    for i in range(26):
        for j in range(len(countList[i])):
            sortedList.append(countList[i][j])
    return sortedList

def radixSort(list, length):
    #For each index, count sort
    for i in range(length, 0, -1):
        list=countSort(list, i-1)
    return list

def largestAnagram(dict):
    d = (open(dict, "r"))
    dictLen=[]
    max=0
    #Copy all words into an array with their lengths
    for word in d:
        word=word.replace("\n","")
        dictLen.append([word, len(word)])
        #Find number of characters in longest word
        if len(word)>max:
            max=len(word)
    #For each number of characters up to max, create empty array
    sortedLen=[[]for i in range(max+1)]
    #For each word, append word to index corresponding to word length
    for word in dictLen:
        sortedLen[word[1]].append(word[0])
    #Sort lists for all word lengths
    for i in range(len(sortedLen)):
        sortedLen[i]=radixSort(sortedLen[i], i)
    #Sort all words such that their characters are in alphabetical order
    sortedWordLen=[[] for i in range(len(sortedLen))]
    for i in range(len(sortedLen)):
        for word in sortedLen[i]:
            sortedWordLen[i].append(countSort(list(word),0))
    #Anagrams kept sorted by length
    anagrams=[[] for i in range((len(sortedLen)))]
    #Keeping track of largest group of anagrams, length of strings in that group, and index to find list
    anaMax=[0, 0, 0]

    #For each word length
    for i in range(len(sortedLen)):
        #For each word in sorted by word length
        for j in range(len(sortedWordLen[i])):
            #For each combination of letters in lengths
            included=False
            if len(anagrams[i])>0:
                for k in range(len(anagrams[i])):
                    #If combination of letters is the same
                    if anagrams[i][k][0]==sortedWordLen[i][j]:
                        included=True
                        anagrams[i][k].append(sortedLen[i][j])
                        if len(anagrams[i][k])>anaMax[0]:
                            anaMax=[len(anagrams[i][k]), i, k]
                if not included:
                    #Add list to anagrams of form [sorted combination of letters, word made of that combination]
                    anagrams[i].append([sortedWordLen[i][j], sortedLen[i][j]])
            else:
                #If empty, add combination and word
                anagrams[i].append([sortedWordLen[i][j], sortedLen[i][j]])
            included=False
    if anaMax[0]!=0:
        #Clean up for printing
        anaMax=str(anagrams[anaMax[1]][anaMax[2]][1:])
        anaMax=anaMax.replace("[","")
        anaMax=anaMax.replace("'","")
        anaMax=anaMax.replace("]","")
        print("The largest group of anagrams is: "+anaMax)
    else:
        print("Dictionary is empty.")
    #Return lists for later use
    return [sortedLen, sortedWordLen, anagrams]

#Function to sort list of anagrams
#Not part of any of the tasks; just set up
def sortAnagrams(anagrams):
    #Empty list to be returned
    sorted=[[]for l in range(len(anagrams))]
    #For each length in anagrams
    for l in range(len(anagrams)):
        temp=[]
        if anagrams[l]!=[]:
            #For each combination of length l
            for j in range(len(anagrams[l])):
                #Add combination to temp
                temp.append(anagrams[l][j][0])
            #Radix sort
            temp=radixSort(temp, l)
            #For each element of sorted combinations
            for e in temp:
                found=False
                index=0
                #Until match is found
                while not found:
                    #if match is found, append combination + words with that combination
                    if anagrams[l][index][0]==e:
                        sorted[l].append(anagrams[l][index])
                        found=True
                    else:
                        index+=1
    return sorted

def getScrabbleWords(anagrams, query):
    #Sort query string into alphabetical order O(k)
    sortQuery=countSort(query,0)
    i=0
    min = 0
    max = len(anagrams[len(query)]) - 1
    found=False
    #Binary search up to all charcaters of query; O(klogN)
    while i < len(sortQuery) and max-min>1:
       mid=(max+min)//2
       if anagrams[len(query)][mid][0][i]==sortQuery[i]:
           if anagrams[len(query)][mid][0]==sortQuery:
               printAna=(anagrams[len(query)][mid][1:])
               return printAna
           else:
               i+=1
       elif anagrams[len(query)][mid][0][i]<sortQuery[i]:
           min=mid
           i=0
       else:
           max=mid
           i=0
    #If no anagrams found
    if not found:
        return None

def getWildCardWords(query, anagrams):
    printAna=[]
    #Add each letter to query and try find anagrams of resulting string
    for letter in "abcdefghijklmonpqrstuvwxyz":
        q=query+letter
        w=getScrabbleWords(anagrams, q)
        if w!=None:
            for word in w:
                printAna.append(word)
    #Sort all possible wildcard anagrams
    printAna=radixSort(printAna, len(query)+1)
    #None found
    if printAna==[]:
        print("There are no words available using a wildcard.")
    #Clean up and print
    else:
        printAna=str(printAna).replace("[","")
        printAna=printAna.replace("]","")
        printAna=printAna.replace("'","")
        print("Words using a wildcard: "+printAna+"\n")

wordLists = largestAnagram("Dictionary.txt")
anagram=sortAnagrams(wordLists[2])
query=input("Enter a query string: ")
while query!="***":
    printAna=getScrabbleWords(anagram, query)
    printAna = str(printAna).replace("[", "")
    printAna = printAna.replace("'", "")
    printAna = printAna.replace("]", "")
    print("\nWords without using a wildcard: "+printAna)
    getWildCardWords(query, anagram)
    query = input("Enter a query string: ")