import React, { useEffect } from 'react'
import { port, protocol } from '../fetchConst';

const ResultPageForAdminAndStudent = () => {
    const uri = `${protocol}://${window.location.hostname}:${port}`;

    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const examId = urlParams.get("examId");
    const partyId = urlParams.get("partyIdOfStudent");
    useEffect(() => {
        checkingResult()
    }, [])
    const checkingResult = () => {
        fetch(`${uri}/onlineexamapplication/control/get-full-result-of-student?partyId=${partyId}&examId=${examId}`,
            { credentials: "include" }
        )
            .then((response) => response.json())
            .then(data => {
                console.log(data)
            })
    }
    console.log(examId)
    return (
        <>

        </>
    )
}

export default ResultPageForAdminAndStudent