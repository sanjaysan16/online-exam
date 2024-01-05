import WarningModal from "../adminSectionContainer/WarningModal";
import { port, protocol } from "../fetchConst";
import Exam from "./Exam";

import { useState, useEffect } from "react";

const ExamDashBord = () => {
  const uri = `${protocol}://${window.location.hostname}:${port}`;

  const [examList, setexamList] = useState([]);
  const [isAdmin, setIsAdmin] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    getExams();
  }, []);

  useEffect(() => {
    getExams();
    adminOrUserCheck();
  }, []);

  const adminOrUserCheck = () => {
    fetch(`${uri}/onlineexamapplication/control/check-admin-or-user`, {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => {
        
        if (data.errMsg == "notAdmin") {
          setIsAdmin(true);
          setMessage(data.message);
          document.getElementById("examdashbordcheck").click();
        }
      })
      .catch((error) => console.error("Error:", error));
  };

  function getExams() {
    fetch(`${uri}/onlineexamapplication/control/get-exam-or-exam-list`)
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
        setexamList(data.Exam_List);
      })
      .catch((error) => console.error("Error:", error));
  }
  console.log(examList.length + "this is length");
  const value = "home";
  return (
    <>
      <p
        id="examdashbordcheck"
        data-bs-toggle="modal"
        data-bs-target="#exampleModal"
      ></p>
      <WarningModal message={message} />
      {isAdmin ? (
        <></>
      ) : (
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
      )}
    </>
  );
};

export default ExamDashBord;
