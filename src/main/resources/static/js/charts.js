document.addEventListener('DOMContentLoaded', () => {
    console.log("DOMContentLoaded event fired");

    const container = document.getElementById('charts-container');
    if (!container) {
        console.error("Charts container not found!");
        return;
    }
    console.log("Charts container found:", container);

    const surveyId = container.dataset.surveyId;
    if (!surveyId) {
        console.error("Survey ID not found in data attribute!");
        return;
    }
    console.log(`Fetching chart data for survey ID: ${surveyId}`);

    fetch(`/survey/${surveyId}/charts/data`)
        .then(response => {
            console.log(`Received response with status: ${response.status}`);
            if (!response.ok) {
                console.error(`HTTP error! Status: ${response.status}`);
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Chart data received:", data);

            const keys = Object.keys(data);
            console.log(`Processing ${keys.length} chart data keys:`, keys);

            keys.forEach(key => {
                console.log(`Processing chart data for key: ${key}`);

                if (!key.startsWith('question_')) return;

                const questionData = data[key];
                const questionName = questionData.questionName;
                const questionType = questionData.type;

                const canvas = document.createElement('canvas');
                canvas.style.width = "400px";
                canvas.style.height = "300px";
                canvas.style.maxWidth = "100%";
                canvas.style.marginTop = "5px";
                canvas.style.marginBottom = "50px";
                canvas.style.display = "block";

                container.appendChild(canvas);
                console.log(`Canvas created and appended for key: ${key}`);

                if (questionType === "numeric") {
                    const stats = questionData.statistics;
                    console.log(`Numeric data stats for ${key}:`, stats);
                    new Chart(canvas, {
                        type: 'bar',
                        data: {
                            labels: ['Mean', 'Median', 'Std Dev', 'Min', 'Max'],
                            datasets: [{
                                label: 'Statistics',
                                data: [stats.mean, stats.median, stats.stdDev, stats.min, stats.max],
                                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                borderColor: 'rgba(75, 192, 192, 1)',
                                borderWidth: 1
                            }]
                        },
                        options: {
                            plugins: {
                                title: {
                                    display: true,
                                    text: questionName,
                                    padding: {
                                        top: 50,
                                        bottom: 20
                                    },
                                    font: {
                                        family: 'Arial',
                                        size: 20
                                    },
                                    color: '#000000'
                                }
                            }
                        }
                    });
                } else if (questionType === "multiple_choice") {
                    const percentages = questionData.percentages;
                    console.log(`Multiple choice data for ${key}:`, percentages);
                    new Chart(canvas, {
                        type: 'pie',
                        data: {
                            labels: Object.keys(percentages),
                            datasets: [{
                                data: Object.values(percentages),
                                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0']
                            }]
                        },
                        options: {
                            plugins: {
                                title: {
                                    display: true,
                                    text: questionName,
                                    padding: {
                                        top: 50,
                                        bottom: 20
                                    },
                                    font: {
                                        family: 'Arial',
                                        size: 20,
                                    },
                                    color: '#000000'
                                }
                            }
                        }
                    });
                } else {
                    console.warn(`Unhandled key type for ${key}`);
                }
            });
        })
        .catch(error => {
            console.error('Error fetching chart data:', error);
        });
});
