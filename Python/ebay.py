import copy

def solve_task1(product_list, price_limit):
    memo = [[0, 0] for i in range(price_limit+1)]
    # For each price up to the price limit
    for i in range(price_limit+1):
        # Keep track of items, prices, profits in optimal solution
        maxItems = [0 for i in range(len(product_list))]
        maxProf = 0
        # For each product
        for j in range(len(product_list)):
            # If product price is below limit
            if product_list[j][2] <= i:
                # Find product value + max value of remaining price
                currentProf = product_list[j][3] + memo[i - product_list[j][2]][0]
                # Find list of items in optimal solution of remaining price
                items = copy.copy(memo[i - product_list[j][2]][1])
                # Add item to list of products
                items[j] += 1
                # If better than current best solution
                if currentProf > maxProf:
                    # Update best profit
                    maxProf = currentProf
                    # Update items in optimal solution
                    maxItems = items
        memo[i] = [maxProf, maxItems]
    to_sell=[]
    for i in range(len(product_list)):
        if memo[price_limit][1][i]>0:
            to_sell.append([memo[price_limit][1][i], i])
    print_solution(to_sell, product_list)

def solve_task2(product_list, price_limit, item_limit):
    #Constructing an array of size 2*price_limit; each row contains price_limit*[0, 0, 0, 0]
    memo=[[[0,[0 for i in range(len(product_list))]] for i in range(price_limit+1)] for i in range(2)]
    #For every number of items up to item_limit
    for i in range(1, item_limit+1):
        #For every price up to price limit
        for j in range(price_limit+1):
            #Keep track of optimal solution
            maxProf=0
            maxItems=[0 for i in range(len(product_list))]
            #For every product in the list
            for k in range(len(product_list)):
                #If product price is below current price
                if product_list[k][2]<=j:
                    #Set current profit, items, price, count
                    currentProf=memo[0][j-product_list[k][2]][0]+product_list[k][3]
                    items=copy.copy(memo[0][j-product_list[k][2]][1])
                    items[k]+=1
                    #If current profit > max profit, update numbers
                    if currentProf>maxProf:
                        maxProf=currentProf
                        maxItems=items
            #Record best solutions for prices in array
            memo[1][j]=[maxProf, maxItems]
        #Copy second row of array into the first to use the next one whilst saving space
        memo[0]=copy.copy(memo[1])
    #Prepare for printing
    to_sell = []
    for i in range(len(product_list)):
        if memo[1][price_limit][1][i] > 0:
            to_sell.append([memo[1][price_limit][1][i], i])
    print_solution(to_sell, product_list)

#####################################################
# DO NOT CHANGE/WRITE ANYTHING BELOW THIS LINE
#####################################################

# This function prints the results in the required format.
# to_sell is a list of lists representing the products to be sold. Each item in to_sell is a list where item[1] is the product ID and item[0] is the quanity of the product.
def print_solution(to_sell,product_list):
    total_items, total_price, total_profit = 0, 0, 0
    for value in to_sell:
        quantity = value[0]
        product = product_list[value[1]]
        print(str(quantity)+" X ["+str(value[1])+":"+product[1]+":"+str(product[2])+":"+str(product[3])+"]")
        total_items += quantity
        total_price += product[2]*quantity
        total_profit += product[3]*quantity
        
    print("Total items sold:",total_items)
    print("Total price of items sold:",total_price)
    print("Total profit of items sold:",total_profit)


input_file = open("products.txt")
product_list = []

i=0
for line in input_file:
    line = line.strip()
    line = line.split(":")
    product_list.append([i,line[1],int(line[2]),int(line[3])])
    i+=1

    
price_limit = int(input("Enter the price limit: "))
print() # the sample solution shown in assignment sheet doesn't have an extra line. But don't worry about it. This is required for the tester. Do not remove this.
item_limit = input("Enter the item limit: ")
print()


if item_limit == "infinity":
    solve_task1(product_list,price_limit)
else:
    try:
        item_limit = int(item_limit)
        solve_task2(product_list,price_limit,item_limit)
    except ValueError:
        print("You must either enter an integer OR infinity")
        
    

