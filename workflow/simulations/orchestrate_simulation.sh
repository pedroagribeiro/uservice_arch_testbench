#!/bin/bash

provision_numbers=(100 200 300)
algorithms=(1 2 3 4)
worker_arragements=(3 5 7) 
sequences=(1 2 3)

perform_request () {
    curl -X POST -H "Content-Type: application/json" \
        -d "{\"workers\": $1, \"olts\": 5, \"algorithm\": $2, \"sequence\": $3, \"messages\": $4}" \
        http://localhost:8080/orchestration
}

for w in ${worker_arragements[@]}; do
    for s in ${sequences[@]}; do
        for a in ${algorithms[@]}; do
            for n in ${provision_numbers[@]}; do
                perform_request $w $a $s $n
                echo ''
                sleep 1
            done
        done
    done
done