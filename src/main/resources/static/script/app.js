(function () {
    var account = document.getElementById('accountNumberMonth');
    var paymentDate = document.getElementById('paymentDate');
    var interest = document.getElementById('amountMonth');
    var interestHint = document.getElementById('interestForDays');

    function refreshInterest() {
        if (!account || !paymentDate || !interest) {
            return;
        }
        var number = account.value.trim();
        if (!number) {
            if (interestHint) {
                interestHint.textContent = '';
            }
            return;
        }
        var params = new URLSearchParams();
        params.set('accountNumberMonth', number);
        params.set('paymentDate', paymentDate.value);
        fetch('/ajaxAction?' + params.toString(), {headers: {'Accept': 'application/json'}})
            .then(function (response) {
                return response.ok ? response.json() : null;
            })
            .then(function (data) {
                if (!data) {
                    return;
                }
                if (data.Interest !== undefined) {
                    interest.value = data.Interest;
                }
                if (interestHint) {
                    interestHint.textContent = data.InterestForDays || '';
                }
            })
            .catch(function () {
                // Interest lookup is a convenience; keep the manually entered value.
            });
    }

    if (account && paymentDate && interest) {
        account.addEventListener('change', refreshInterest);
        paymentDate.addEventListener('change', refreshInterest);
        if (account.value.trim()) {
            refreshInterest();
        }
    }

    document.querySelectorAll('[data-confirm]').forEach(function (button) {
        button.addEventListener('click', function (event) {
            if (!window.confirm(button.getAttribute('data-confirm'))) {
                event.preventDefault();
            }
        });
    });

    document.querySelectorAll('[data-print]').forEach(function (button) {
        button.addEventListener('click', function () {
            window.print();
        });
    });
})();
