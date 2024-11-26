document.addEventListener('DOMContentLoaded', () => {
    console.log("DOMContentLoaded event fired"); // Debug log

    const container = document.getElementById('charts-container');
    if (!container) {
        console.error("Charts container not found!"); // Debug log
        return;
    }
    console.log("Charts container found:", container); // Debug log

    const surveyId = container.dataset.surveyId;
    if (!surveyId) {
        console.error("Survey ID not found in data attribute!"); // Debug log
        return;
    }
    console.log(`Fetching chart data for survey ID: ${surveyId}`); // Debug log

    fetch(`/survey/${surveyId}/charts/data`)
        .then(response => {
            console.log(`Received response with status: ${response.status}`); // Debug log
            if (!response.ok) {
                console.error(`HTTP error! Status: ${response.status}`); // Debug log
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Chart data received:", data); // Debug log

            const keys = Object.keys(data);
            console.log(`Processing ${keys.length} chart data keys:`, keys); // Debug log

            keys.forEach(key => {
                console.log(`Processing chart data for key: ${key}`); // Debug log

                const canvas = document.createElement('canvas');

                // Apply CSS styles to each canvas
                canvas.style.width = "400px";
                canvas.style.height = "300px";
                canvas.style.maxWidth = "100%";
                canvas.style.margin = "20px auto";
                canvas.style.display = "block";

                container.appendChild(canvas);
                console.log(`Canvas created and appended for key: ${key}`); // Debug log

                if (key.startsWith('numeric_')) {
                    const stats = data[key];
                    console.log(`Numeric data stats for ${key}:`, stats); // Debug log
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
                                    text: 'Custom Chart Title',
                                    padding: {
                                        top: 50,
                                        bottom: 20
                                    }
                                }
                            }
                        }
                    });
                } else if (key.startsWith('multiple_choice_')) {
                    const percentages = data[key];
                    console.log(`Multiple choice data for ${key}:`, percentages); // Debug log
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
                                    text: 'Custom Chart Title',
                                    padding: {
                                        top: 50,
                                        bottom: 20
                                    }
                                }
                            }
                        }
                    });
                } else {
                    console.warn(`Unhandled key type for ${key}`); // Debug log
                }
            });
        })
        .catch(error => {
            console.error('Error fetching chart data:', error); // Debug log
        });
});
