import Exam from "./Exam";

import { useState, useEffect } from "react";

const ExamDashBord = () => {
  const [examList, setexamList] = useState([]);

  useEffect(() => {
    getExams();
  }, []);

  function getExams() {
    fetch(
      `https://localhost:8443/onlineexamapplication/control/get-exam-or-exam-list`
    )
      .then((res) => res.json())
      .then((data) => {
        setexamList(data.Exam_List);
      })
      .catch((error) => console.error("Error:", error));
  }
  console.log(examList.length + "this is length");
  const value = "home";
  return (
    <div className="pb-5">
      <div className="container-fluid py-5 text-center overflow-hidden">
        <div className="card w-100%">
          <div className="card-header ">
            <h1 style={{ float: "left" }}>Exams</h1>
            <a href="/add-exam">
              <button
                className="btn-outline-success p-2 btn m-2"
                type="button"
                data-toggle="tooltip"
                data-placement="bottom"
                title="AddExam"
                style={{ float: "right" }}
              >
                {" "}
                AddExam
              </button>
            </a>
          </div>
          {examList.length === 0 ? (
            <div className="p-5">
              <h1>no content available</h1>
            </div>
          ) : (
            <div className="card-body ">
              {examList.map((exam, index) => {
                return (
                  <div className="row m-3 justify-content-evenly">
                    <Exam key={index} exam={exam} />
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ExamDashBord;
