f = open('C:/Users/John/Documents/GitHub/OrionsBelt/textAnalytics/src/main/MovieReviews/negative-words.txt', 'r')
f2 = open('C:/Users/John/Documents/GitHub/OrionsBelt/textAnalytics/src/main/MovieReviews/negative-words-halved.txt', 'a')
f3 = open('C:/Users/John/Documents/GitHub/OrionsBelt/.provenanceRewrite/src/main/MovieReviews/negative-words-halved.txt', 'a')
add_line = 0

for line in f:
    if add_line == 0:
        add_line = 1
    elif add_line == 1:
        add_line = 0
        f2.write(line)
        f3.write(line)

f.close()
f2.close()
f3.close()
