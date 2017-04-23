

"""
An alternative approach to pattern matching. Creating a k-mer index of the 
dictionary to quickly search for the word (using binary search)
"""


word_list1 = ['ggggggesneriaceous','aardvark','altimeter','apotactic','bagonet',
              'boatlip','carburant', 'chyliferous','consonance','cyclospondylic',
              'dictyostele','dictyostele','echelon',
              'estadal','flaunty','gesneriaceous','hygienic','infracentral',
              'jipijapa', 'lipoceratous','melanthaceae']
                  
word_list2 = ['angiohydrotomy','angiospermal','anglist','angularization','anhungry',
              'animalcule','anisaldehyde', 'anisometric', 'ankylenteron', 'annette']
            
word_list3 = ['bagonet','consonance','estadal','hygienic','melanthaceae','overwander',
              'prototypographer','siphonocladales', 'transferography', 
              'venturesomeness']
              
word_list4 = ['anisaldehyde','anisometric','ankylenteron']


import bisect
class Autocomplete(object):
    #Has 2 methods: 1st is a initialization method that preprocesses the string
    #and the second a query function

    def __init__(self,k, word_list = list()): #t =text, k = k-mer length 
        self.word_list = ''.join(word_list)
        self.k = k #defining class variables
        self.index = []  #empty list
        
        #loop through every the text and get every k-mer and its index
        for i in range(len(self.word_list)-k +1): #every alignment (index)
            self.index.append((self.word_list[i:i+k], i))  #tuple = 2 associated values
        self.index.sort() #sort the index
        
        global word_string; global pattern;     
        word_string = self.word_list        
        pattern = input('Please input the pattern you would like to find: ').lower() 
        
    def query(self,pattern):
        kmer = pattern[:self.k]
        i = bisect.bisect_left(self.index, (kmer, -1)) #get first occurrence of all indexs greater than 1
        hits  = []
        while i < len(self.index):
            if self.index[i][0] != kmer:
                break
            hits.append(self.index[i][1])
            i +=1
        return hits
        
    def queryIndex(pattern, word_string,index): #index created from t
        k = index.k
        offsets = []
        for i in index.query(pattern):
            if pattern[k:] == word_string[i+k: i+len(pattern)]:
                offsets.append(i) #verification that p matches in its entirety
        
        return ('The pattern was found at index/s: %s ' %offsets)


index = Autocomplete(5,word_list2)
Autocomplete.queryIndex(pattern, word_string, index)
 
