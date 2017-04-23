

"""
Message Construction: Can you build a word using a string?
"""


string = 'Merry Christmas'
word = 'Mkkkrry'

class find():
       
    #Step #1
    def index(self,string):
        letters = {}
        length = len(string)

        for i in range(length):
            if string[i] not in letters:
                letters[string[i]] = 1
            else:
                letters[string[i]] +=1

        return letters #return dictionary

    #Step #2
    def match(self,letters, word):
        construct = True
        length = len(word)

        for i in range(length):
            if word[i] not in letters.keys():
                construct = False
                return construct

            if letters[word[i]] == 0:
                construct = False
                return construct

            letters[word[i]] -= 1


        return construct
    
    
def implement():
    
    a = find()
    one = a.index(string)
    two = a.match(one,word)

    return two

implement()

    

