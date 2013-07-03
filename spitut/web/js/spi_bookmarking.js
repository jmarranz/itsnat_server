
function SPISite()
{
    this.load = load;
    this.detectURLChange = detectURLChange;
    this.detectURLChangeCB = detectURLChangeCB;
    this.setURLReference = setURLReference;
    this.onBackForward = null; // Public, user defined

    this.firstTime = true;
    this.initialURLWithState = null;
    this.href = null;
    this.disabled = false; 
    
    this.load();

    function load() // page load phase
    {
        if (this.disabled) return;

        var url = window.location.href;
        var posR = url.lastIndexOf("#!st=");
        if (posR == -1) return;
        var state = url.substring(posR + "#!st=".length);
        if (state == "") return;
        this.initialURLWithState = window.location.href;
    }

    function setURLReference(stateName)
    {
        if (this.disabled) return;

        var url = window.location.href;
        var posR = url.lastIndexOf("#");
        if (posR != -1) url = url.substring(0,posR);
        url = url + "#!st=" + stateName;
        if (url != window.location.href)
            window.location.href = url;

        this.href = window.location.href;

        if (!this.firstTime) return;

        this.firstTime = false;
        if (this.initialURLWithState != null)
        {
            // Loads the initial state in URL if different to default
            window.location.href = this.initialURLWithState;
            this.initialURLWithState = null;
            this.detectURLChange(1); // As soon as possible
        }
        else this.detectURLChange(200);
    }

    function detectURLChange(time)
    {
        var func = function()
        {
            arguments.callee.spiSite.detectURLChangeCB();
            window.setTimeout(arguments.callee,200);
        };
        func.spiSite = this;
        window.setTimeout(func,time);
    }

    function detectURLChangeCB()
    {
        // Detecting when only the reference part of the URL changes
        var url = window.location.href;
        if (this.href == url) return;
        var posR = url.lastIndexOf("#!st=");
        if (posR == -1) return;
        var posR2 = this.href.lastIndexOf("#!st=");
        if (posR != posR2) return;
        var url2 = url.substring(0,posR);
        var spiHref2 = this.href.substring(0,posR);
        if (url2 != spiHref2) return;

        // Only changed the reference part
        this.href = url;

        var stateName = url.substring(posR + "#!st=".length);
        if (this.onBackForward) this.onBackForward(stateName);
        else try { window.location.reload(true); }
             catch(ex) { window.location = window.location; }
    }
}

window.spiSite = new SPISite();

