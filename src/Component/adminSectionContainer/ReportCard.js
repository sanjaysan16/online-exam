import React, { useEffect, useState } from "react";
import Exam from "../examSectionContainer/Exam";
import { port, protocol } from "../fetchConst";
import Result from "../userSectionContainer/Result";

const ReportCard = () => {
  const [examList, setExamList] = useState([]);
  const [name, setName] = useState()
  //const [examName, setExamName] = useState()

  const uri = `${protocol}://${window.location.hostname}:${port}`;

  useEffect(() => {
    getUserExamMappingList();
    // adminOrUserCheck();
  }, []);
  const getUserExamMappingList = async () => {
    const userExamMappedFetch = await fetch('https://localhost:8443/onlineexamapplication/control/get-list-of-user-exam-mapping', {
      credentials: 'include'
    });
    const userExamMappedList = await userExamMappedFetch.json();
    console.log(userExamMappedFetch);
    setExamList(userExamMappedList.mappedAndUnMappedExams.listOfMappedExams);
  }


  // useEffect(() => {
  //   getExams();
  // }, []);

  // function getExams() {
  //   fetch(`${uri}/onlineexamapplication/control/get-exam-or-exam-list`)
  //     .then((res) => res.json())
  //     .then((data) => {
  //       console.log(data);

  //       setexamList(data.Exam_List);
  //     })
  //     .catch((error) => console.error("Error:", error));
  // }
  useEffect(() => {
    getName()
  }, []);

  const getName = () => {
    fetch(`${uri}/onlineexamapplication/control/check-login`, {
      credentials: "include",
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        const responseName = data.userFirstName;

        setName(responseName);

      })
      .catch((error) => console.error("Error:", error));
  };


  return (
    <>

      <div class="card m-4 p-5">
        <div class="card-header">
          <h3>Exams</h3>
        </div>
        {examList.length === 0 ? (
          <div className="p-5">
            <h1>no content available</h1>
          </div>
        ) : (
          <div className="card-body ">
            {examList.map((exam) => {
              return (
                <div className="row m-3 justify-content-evenly">
                  <ul class="list-group list-group-flush">
                    <li class="list-group-item border">
                      {exam.examName}
                      <a href={`result?name=${name}&examName=${exam.examName}`}>
                        <button
                          className="btn btn-danger mx-3"
                          style={{ float: "right" }}
                        >
                          result
                        </button>
                      </a>

                    </li>
                  </ul>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </>
  );
};

export default ReportCard;
